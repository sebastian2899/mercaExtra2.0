package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.ProductoFavoritos}.
 */
public interface ProductoFavoritosService {
    /**
     * Save a productoFavoritos.
     *
     * @param productoFavoritosDTO the entity to save.
     * @return the persisted entity.
     */
    ProductoFavoritosDTO save(ProductoFavoritosDTO productoFavoritosDTO);

    /**
     * Partially updates a productoFavoritos.
     *
     * @param productoFavoritosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductoFavoritosDTO> partialUpdate(ProductoFavoritosDTO productoFavoritosDTO);

    /**
     * Get all the productoFavoritos.
     *
     * @return the list of entities.
     */
    List<ProductoFavoritosDTO> findAll();

    /**
     * Get the "id" productoFavoritos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductoFavoritosDTO> findOne(Long id);

    /**
     * Delete the "id" productoFavoritos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductoDTO> favoriteProducts();
}
