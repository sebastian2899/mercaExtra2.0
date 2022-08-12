package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.ProductoPromocionHome;
import com.mercaextra.app.repository.ProductoPromocionHomeRepository;
import com.mercaextra.app.repository.ProductoRepository;
import com.mercaextra.app.service.ProductoPromocionHomeService;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoPromocionHomeDTO;
import com.mercaextra.app.service.mapper.ProductoMapper;
import com.mercaextra.app.service.mapper.ProductoPromocionHomeMapper;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductoPromocionHome}.
 */
@Service
@Transactional
public class ProductoPromocionHomeServiceImpl implements ProductoPromocionHomeService {

    private final Logger log = LoggerFactory.getLogger(ProductoPromocionHomeServiceImpl.class);

    private final ProductoPromocionHomeRepository productoPromocionHomeRepository;

    private final ProductoPromocionHomeMapper productoPromocionHomeMapper;

    private final ProductoRepository productoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductoMapper productoMapper;

    public ProductoPromocionHomeServiceImpl(
        ProductoPromocionHomeRepository productoPromocionHomeRepository,
        ProductoPromocionHomeMapper productoPromocionHomeMapper,
        ProductoRepository productoRepository
    ) {
        this.productoPromocionHomeRepository = productoPromocionHomeRepository;
        this.productoPromocionHomeMapper = productoPromocionHomeMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoPromocionHomeDTO save(ProductoPromocionHomeDTO productoPromocionHomeDTO) {
        log.debug("Request to save ProductoPromocionHome : {}", productoPromocionHomeDTO);
        ProductoPromocionHome productoPromocionHome = productoPromocionHomeMapper.toEntity(productoPromocionHomeDTO);

        //        ANTES DE GUARDAR UN NUEVO PRODUCTO A LA LISTA DE FAVORITOS DEVEMOS EVALUAR PRIMERO QUE NO EXISTA ACTUALENTE
        //       EL PRODUCTO EN LA LISTA DE PRODUCTOS GUARDADOS EN EL HOME.

        // CASTEAMOS LA RESPUESTA
        boolean resp = Boolean.parseBoolean(productoPromocionHomeRepository.resp(productoPromocionHome.getIdProducto()));
        if (!resp) {
            productoPromocionHome = productoPromocionHomeRepository.save(productoPromocionHome);
        } else {
            throw new BadRequestAlertException("Ya existe en la lista home", "Ya existe en la lista home", "Ya existe en la lista home");
        }

        return productoPromocionHomeMapper.toDto(productoPromocionHome);
    }

    @Override
    public Optional<ProductoPromocionHomeDTO> partialUpdate(ProductoPromocionHomeDTO productoPromocionHomeDTO) {
        log.debug("Request to partially update ProductoPromocionHome : {}", productoPromocionHomeDTO);

        return productoPromocionHomeRepository
            .findById(productoPromocionHomeDTO.getId())
            .map(existingProductoPromocionHome -> {
                productoPromocionHomeMapper.partialUpdate(existingProductoPromocionHome, productoPromocionHomeDTO);

                return existingProductoPromocionHome;
            })
            .map(productoPromocionHomeRepository::save)
            .map(productoPromocionHomeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoPromocionHomeDTO> findAll() {
        log.debug("Request to get all ProductoPromocionHomes");
        return productoPromocionHomeRepository
            .findAll()
            .stream()
            .map(productoPromocionHomeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoPromocionHomeDTO> findOne(Long id) {
        log.debug("Request to get ProductoPromocionHome : {}", id);
        return productoPromocionHomeRepository.findById(id).map(productoPromocionHomeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductoPromocionHome : {}", id);
        productoPromocionHomeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> productosDescuento() {
        log.debug("Request to get disscount products.");
        return productoRepository
            .productosConDescuento()
            .stream()
            .map(productoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional
    public void deleteProductoDesc(Long id) {
        log.debug("Request to delete producto.");
        Query q = entityManager.createQuery("DELETE FROM ProductoPromocionHome WHERE idProducto = :id").setParameter("id", id);
        q.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> productosEnListaHome() {
        log.debug("Request to get all producto in favorite list home");
        return productoRepository
            .productosEnListaHome()
            .stream()
            .map(productoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
