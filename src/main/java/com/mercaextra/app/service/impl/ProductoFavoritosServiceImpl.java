package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.domain.ProductoFavoritos;
import com.mercaextra.app.repository.ProductoFavoritosRepository;
import com.mercaextra.app.repository.ProductoRepository;
import com.mercaextra.app.service.ProductoFavoritosService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import com.mercaextra.app.service.mapper.ProductoFavoritosMapper;
import com.mercaextra.app.service.mapper.ProductoMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductoFavoritos}.
 */
@Service
@Transactional
public class ProductoFavoritosServiceImpl implements ProductoFavoritosService {

    private final Logger log = LoggerFactory.getLogger(ProductoFavoritosServiceImpl.class);

    private final ProductoFavoritosRepository productoFavoritosRepository;

    private final ProductoRepository productoRepository;

    private final ProductoFavoritosMapper productoFavoritosMapper;

    private final UserService userService;

    private final ProductoMapper productoMapper;

    @PersistenceContext
    EntityManager entityManager;

    public ProductoFavoritosServiceImpl(
        ProductoFavoritosRepository productoFavoritosRepository,
        ProductoFavoritosMapper productoFavoritosMapper,
        UserService userService,
        ProductoMapper productoMapper,
        ProductoRepository productoRepository
    ) {
        this.productoFavoritosRepository = productoFavoritosRepository;
        this.productoFavoritosMapper = productoFavoritosMapper;
        this.userService = userService;
        this.productoMapper = productoMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoFavoritosDTO save(ProductoFavoritosDTO productoFavoritosDTO) {
        log.debug("Request to save ProductoFavoritos : {}", productoFavoritosDTO);
        ProductoFavoritos productoFavoritos = productoFavoritosMapper.toEntity(productoFavoritosDTO);
        String login = userService.getUserWithAuthorities().get().getLogin();

        // CONSULTAMOS LA LISTA DE PRODUCTOS FAVORITOS
        List<ProductoFavoritos> producFavoritos = productoFavoritosRepository.favPorLogin(login);

        if (null == productoFavoritos.getId()) {
            productoFavoritos.setLastUpdate(Instant.now());
            productoFavoritos.setLogin(login);
            productoFavoritos.setEstado("Visible");

            if (producFavoritos.size() == 0 || null == producFavoritos) {
                productoFavoritos.setPuesto(1);
            } else if (producFavoritos.size() == 1) {
                productoFavoritos.setPuesto(2);
            } else {
                Query q = entityManager
                    .createQuery("SELECT MAX(pf.puesto) FROM ProductoFavoritos pf WHERE pf.login =:login")
                    .setParameter("login", login);
                int res = Integer.parseInt(q.getSingleResult().toString());
                productoFavoritos.setPuesto(res + 1);
            }
        }

        productoFavoritos = productoFavoritosRepository.save(productoFavoritos);
        return productoFavoritosMapper.toDto(productoFavoritos);
    }

    @Override
    public Optional<ProductoFavoritosDTO> partialUpdate(ProductoFavoritosDTO productoFavoritosDTO) {
        log.debug("Request to partially update ProductoFavoritos : {}", productoFavoritosDTO);

        return productoFavoritosRepository
            .findById(productoFavoritosDTO.getId())
            .map(existingProductoFavoritos -> {
                productoFavoritosMapper.partialUpdate(existingProductoFavoritos, productoFavoritosDTO);

                return existingProductoFavoritos;
            })
            .map(productoFavoritosRepository::save)
            .map(productoFavoritosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoFavoritosDTO> findAll() {
        log.debug("Request to get all ProductoFavoritos");
        return productoFavoritosRepository
            .findAll()
            .stream()
            .map(productoFavoritosMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> favoriteProducts() {
        //RECUPERAMOS EL LOGIN
        String login = userService.getUserWithAuthorities().get().getLogin();

        // List<Producto>productos = productoRepository.findAll();
        // List<ProductoFavoritos>productosFavoritos = productoFavoritosRepository.favPorLogin(login);

        List<Object[]> favorites = productoFavoritosRepository.favoriteProduts(login, "Visible");

        return favorites
            .stream()
            .map(element -> {
                Producto producto = new Producto();
                Producto producto2 = productoRepository.getById(Long.parseLong(element[0].toString()));
                producto.setId(Long.parseLong(element[0].toString()));
                producto.setNombre(element[1].toString());
                producto.setPrecio(new BigDecimal(element[2].toString()));
                // byte[] bytes = SerializationUtils.serialize(element[3].toString());
                producto.setImagen(producto2.getImagen());
                producto.setImagenContentType(producto2.getImagenContentType());
                producto.setCategoria(element[5].toString());
                return producto;
            })
            .map(productoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        /*
         * return productos.stream() .filter(element -> validatedId(element.getId(),
         * productosFavoritos)) .map(productoMapper::toDto)
         * .collect(Collectors.toCollection(LinkedList :: new));
         */
    }

    /*
     * private boolean validatedId(Long id,List<ProductoFavoritos> favorites) {
     * log.debug("Request to validate if the product is favorite");
     *
     * Long resp = favorites.stream() .filter(element -> element.getIdProduct() ==
     * id) .count();
     *
     * boolean retorno = resp == 1 ? true : false;
     *
     * return retorno; }
     */

    @Override
    public List<ProductoDTO> productosOcultos() {
        log.debug("Request to get all hidden fav products");

        String Login = userService.getUserWithAuthorities().get().getLogin();

        List<Object[]> productsObject = productoFavoritosRepository.favoriteProduts(Login, "Oculto");

        return productsObject
            .stream()
            .map(element -> {
                Producto producto = new Producto();
                Producto productoImg = new Producto();

                producto.setId(Long.parseLong(element[0].toString()));
                producto.setNombre(element[1].toString());
                producto.setPrecio(new BigDecimal(element[2].toString()));
                productoImg = productoRepository.getById(Long.parseLong(element[0].toString()));
                producto.setImagen(productoImg.getImagen());
                producto.setImagenContentType(productoImg.getImagenContentType());
                producto.setCategoria(element[3].toString());

                return producto;
            })
            .map(productoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> goFirst(ProductoDTO productoDto) {
        log.debug("Request to change position of product at first");

        String login = userService.getUserWithAuthorities().get().getLogin();

        ProductoFavoritos pf = productoFavoritosRepository.productoFavPorIdProduc(productoDto.getId());

        List<ProductoFavoritos> favs = productoFavoritosRepository.favPorLogin(login);

        favs
            .stream()
            .forEach(element -> {
                element.setPuesto(element.getPuesto() + 1);
                productoFavoritosRepository.save(element);
            });

        pf.setPuesto(1);
        productoFavoritosRepository.save(pf);

        /*
         * productos.stream() .filter(element -> element.getPuesto() != 1) .map(element
         * -> { element.setPuesto(2); return element; });
         */

        /*
         * // ENCONTRAMOS LA POSICION DEL PRODUCTO EN LA LISTA int posProduct =
         * productos.indexOf(pf);
         *
         * // ELIMINAMOS LA POSICINO Y EL PRODUCTO EN LA POSICION DONDE ESTABA.
         * productos.remove(posProduct);
         *
         *
         * // LO MOVEMOS A LA PRIMERA POSICION. productos.add(0,pf);
         */

        // GUARDAMOS LA NUEVA LISTA RESULTANTE
        return favoriteProducts();
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    @Override
    @Transactional(readOnly = true)
    public Instant lastUpdate() {
        log.debug("Reques to get last update");

        Instant lastUpdate = productoFavoritosRepository.lastUpdate();

        return lastUpdate;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoFavoritosDTO> findOne(Long id) {
        log.debug("Request to get ProductoFavoritos : {}", id);
        return productoFavoritosRepository.findById(id).map(productoFavoritosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductoFavoritos : {}", id);

        //ELIMINAMOS EL PRODUCTO FAV COTEMPLANDO EL ID DEL PRODUCTO QUE ESTAMOS ENVIANDO DESDE EL FRONT Y NO EL ID DE LA ENTIDAD PRODUCTO_FAVORITOS
        Query q = entityManager.createQuery("DELETE FROM ProductoFavoritos WHERE idProduct = :idProduct").setParameter("idProduct", id);

        q.executeUpdate();
        // ESTE METODO ELIMINAD AL PRODUCTO FAV POR ID DEL Y NECESITAMOS ELIMNAR EL FAV POR EL ID DEL PRODUCTO AGREGADO A FAV
        //productoFavoritosRepository.deleteById(id);
    }
}
