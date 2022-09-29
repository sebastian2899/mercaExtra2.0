package com.mercaextra.app.repository;

import com.mercaextra.app.domain.UserLikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserLikeComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLikeCommentRepository extends JpaRepository<UserLikeComment, Long> {
    @Query("SELECT ul FROM UserLikeComment ul WHERE ul.login= :login AND ul.idComment = :idComment")
    UserLikeComment userLikePerCommet(@Param("login") String login, @Param("idComment") Long idComment);
}
