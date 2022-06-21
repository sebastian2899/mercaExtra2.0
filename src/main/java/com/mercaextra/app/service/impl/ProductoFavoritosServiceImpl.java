package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.domain.ProductoFavoritos;
import com.mercaextra.app.repository.ProductoFavoritosRepository;
import com.mercaextra.app.service.ProductoFavoritosService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import com.mercaextra.app.service.mapper.ProductoFavoritosMapper;
import com.mercaextra.app.service.mapper.ProductoMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import liquibase.repackaged.org.apache.commons.lang3.SerializationUtils;
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

    private final ProductoFavoritosMapper productoFavoritosMapper;

    private final UserService userService;

    private final ProductoMapper productoMapper;

    @PersistenceContext
    EntityManager entityManager;

    public ProductoFavoritosServiceImpl(
        ProductoFavoritosRepository productoFavoritosRepository,
        ProductoFavoritosMapper productoFavoritosMapper,
        UserService userService,
        ProductoMapper productoMapper
    ) {
        this.productoFavoritosRepository = productoFavoritosRepository;
        this.productoFavoritosMapper = productoFavoritosMapper;
        this.userService = userService;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoFavoritosDTO save(ProductoFavoritosDTO productoFavoritosDTO) {
        log.debug("Request to save ProductoFavoritos : {}", productoFavoritosDTO);
        ProductoFavoritos productoFavoritos = productoFavoritosMapper.toEntity(productoFavoritosDTO);
        String login = userService.getUserWithAuthorities().get().getLogin();
        productoFavoritos.setLastUpdate(Instant.now());
        productoFavoritos.setLogin(login);
        productoFavoritos.setEstado("Visible");

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

        List<Object[]> favorites = productoFavoritosRepository.favoriteProduts(login);

        List<Producto> products = favorites
            .stream()
            .map(element -> {
                Producto producto = new Producto();
                producto.setId(Long.parseLong(element[0].toString()));
                producto.setNombre(element[1].toString());
                producto.setPrecio(new BigDecimal(element[2].toString()));
                byte[] bytes = SerializationUtils.serialize(element[3].toString());
                producto.setImagen(bytes);
                producto.setCategoria(element[4].toString());

                return producto;
            })
            .collect(Collectors.toCollection(LinkedList::new));

        return productoMapper.toDto(products);
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
