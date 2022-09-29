package com.mercaextra.app.service;

import com.mercaextra.app.domain.UserLikeComment;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserLikeComment}.
 */
public interface UserLikeCommentService {
    /**
     * Save a userLikeComment.
     *
     * @param userLikeComment the entity to save.
     * @return the persisted entity.
     */
    UserLikeComment save(UserLikeComment userLikeComment);

    /**
     * Partially updates a userLikeComment.
     *
     * @param userLikeComment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserLikeComment> partialUpdate(UserLikeComment userLikeComment);

    /**
     * Get all the userLikeComments.
     *
     * @return the list of entities.
     */
    List<UserLikeComment> findAll();

    /**
     * Get the "id" userLikeComment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserLikeComment> findOne(Long id);

    /**
     * Delete the "id" userLikeComment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
