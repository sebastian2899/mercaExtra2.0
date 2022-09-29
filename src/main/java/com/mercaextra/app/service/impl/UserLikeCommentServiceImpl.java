package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.UserLikeComment;
import com.mercaextra.app.repository.UserLikeCommentRepository;
import com.mercaextra.app.service.UserLikeCommentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserLikeComment}.
 */
@Service
@Transactional
public class UserLikeCommentServiceImpl implements UserLikeCommentService {

    private final Logger log = LoggerFactory.getLogger(UserLikeCommentServiceImpl.class);

    private final UserLikeCommentRepository userLikeCommentRepository;

    public UserLikeCommentServiceImpl(UserLikeCommentRepository userLikeCommentRepository) {
        this.userLikeCommentRepository = userLikeCommentRepository;
    }

    @Override
    public UserLikeComment save(UserLikeComment userLikeComment) {
        log.debug("Request to save UserLikeComment : {}", userLikeComment);
        return userLikeCommentRepository.save(userLikeComment);
    }

    @Override
    public Optional<UserLikeComment> partialUpdate(UserLikeComment userLikeComment) {
        log.debug("Request to partially update UserLikeComment : {}", userLikeComment);

        return userLikeCommentRepository
            .findById(userLikeComment.getId())
            .map(existingUserLikeComment -> {
                if (userLikeComment.getIdComment() != null) {
                    existingUserLikeComment.setIdComment(userLikeComment.getIdComment());
                }
                if (userLikeComment.getLogin() != null) {
                    existingUserLikeComment.setLogin(userLikeComment.getLogin());
                }
                if (userLikeComment.getIsLike() != null) {
                    existingUserLikeComment.setIsLike(userLikeComment.getIsLike());
                }

                return existingUserLikeComment;
            })
            .map(userLikeCommentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLikeComment> findAll() {
        log.debug("Request to get all UserLikeComments");
        return userLikeCommentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserLikeComment> findOne(Long id) {
        log.debug("Request to get UserLikeComment : {}", id);
        return userLikeCommentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserLikeComment : {}", id);
        userLikeCommentRepository.deleteById(id);
    }
}
