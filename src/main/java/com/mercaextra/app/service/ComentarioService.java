package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ComentarioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Comentario}.
 */
public interface ComentarioService {
    /**
     * Save a comentario.
     *
     * @param comentarioDTO the entity to save.
     * @return the persisted entity.
     */
    ComentarioDTO save(ComentarioDTO comentarioDTO);

    /**
     * Partially updates a comentario.
     *
     * @param comentarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComentarioDTO> partialUpdate(ComentarioDTO comentarioDTO);

    /**
     * Get all the comentarios.
     *
     * @return the list of entities.
     */
    List<ComentarioDTO> findAll();

    /**
     * Get the "id" comentario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComentarioDTO> findOne(Long id);

    /**
     * Delete the "id" comentario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ComentarioDTO> uploadCommentsProduct(Long idProducto);

    List<ComentarioDTO> responseComments(Long idProducto, Long idComentario);
}
