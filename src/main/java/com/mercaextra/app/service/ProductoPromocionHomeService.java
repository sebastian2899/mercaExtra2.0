package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoPromocionHomeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.ProductoPromocionHome}.
 */
public interface ProductoPromocionHomeService {
    /**
     * Save a productoPromocionHome.
     *
     * @param productoPromocionHomeDTO the entity to save.
     * @return the persisted entity.
     */
    ProductoPromocionHomeDTO save(ProductoPromocionHomeDTO productoPromocionHomeDTO);

    /**
     * Partially updates a productoPromocionHome.
     *
     * @param productoPromocionHomeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductoPromocionHomeDTO> partialUpdate(ProductoPromocionHomeDTO productoPromocionHomeDTO);

    /**
     * Get all the productoPromocionHomes.
     *
     * @return the list of entities.
     */
    List<ProductoPromocionHomeDTO> findAll();

    /**
     * Get the "id" productoPromocionHome.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductoPromocionHomeDTO> findOne(Long id);

    /**
     * Delete the "id" productoPromocionHome.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductoDTO> productosDescuento();

    List<ProductoDTO> productosEnListaHome();
}
