package com.mercaextra.app.web.rest;

import com.mercaextra.app.domain.UserLikeComment;
import com.mercaextra.app.repository.UserLikeCommentRepository;
import com.mercaextra.app.service.UserLikeCommentService;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.UserLikeComment}.
 */
@RestController
@RequestMapping("/api")
public class UserLikeCommentResource {

    private final Logger log = LoggerFactory.getLogger(UserLikeCommentResource.class);

    private static final String ENTITY_NAME = "userLikeComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserLikeCommentService userLikeCommentService;

    private final UserLikeCommentRepository userLikeCommentRepository;

    public UserLikeCommentResource(UserLikeCommentService userLikeCommentService, UserLikeCommentRepository userLikeCommentRepository) {
        this.userLikeCommentService = userLikeCommentService;
        this.userLikeCommentRepository = userLikeCommentRepository;
    }

    /**
     * {@code POST  /user-like-comments} : Create a new userLikeComment.
     *
     * @param userLikeComment the userLikeComment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userLikeComment, or with status {@code 400 (Bad Request)} if the userLikeComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-like-comments")
    public ResponseEntity<UserLikeComment> createUserLikeComment(@RequestBody UserLikeComment userLikeComment) throws URISyntaxException {
        log.debug("REST request to save UserLikeComment : {}", userLikeComment);
        if (userLikeComment.getId() != null) {
            throw new BadRequestAlertException("A new userLikeComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserLikeComment result = userLikeCommentService.save(userLikeComment);
        return ResponseEntity
            .created(new URI("/api/user-like-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-like-comments/:id} : Updates an existing userLikeComment.
     *
     * @param id the id of the userLikeComment to save.
     * @param userLikeComment the userLikeComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLikeComment,
     * or with status {@code 400 (Bad Request)} if the userLikeComment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userLikeComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-like-comments/{id}")
    public ResponseEntity<UserLikeComment> updateUserLikeComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserLikeComment userLikeComment
    ) throws URISyntaxException {
        log.debug("REST request to update UserLikeComment : {}, {}", id, userLikeComment);
        if (userLikeComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLikeComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userLikeCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserLikeComment result = userLikeCommentService.save(userLikeComment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userLikeComment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-like-comments/:id} : Partial updates given fields of an existing userLikeComment, field will ignore if it is null
     *
     * @param id the id of the userLikeComment to save.
     * @param userLikeComment the userLikeComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLikeComment,
     * or with status {@code 400 (Bad Request)} if the userLikeComment is not valid,
     * or with status {@code 404 (Not Found)} if the userLikeComment is not found,
     * or with status {@code 500 (Internal Server Error)} if the userLikeComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-like-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserLikeComment> partialUpdateUserLikeComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserLikeComment userLikeComment
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserLikeComment partially : {}, {}", id, userLikeComment);
        if (userLikeComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLikeComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userLikeCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserLikeComment> result = userLikeCommentService.partialUpdate(userLikeComment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userLikeComment.getId().toString())
        );
    }

    /**
     * {@code GET  /user-like-comments} : get all the userLikeComments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userLikeComments in body.
     */
    @GetMapping("/user-like-comments")
    public List<UserLikeComment> getAllUserLikeComments() {
        log.debug("REST request to get all UserLikeComments");
        return userLikeCommentService.findAll();
    }

    /**
     * {@code GET  /user-like-comments/:id} : get the "id" userLikeComment.
     *
     * @param id the id of the userLikeComment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userLikeComment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-like-comments/{id}")
    public ResponseEntity<UserLikeComment> getUserLikeComment(@PathVariable Long id) {
        log.debug("REST request to get UserLikeComment : {}", id);
        Optional<UserLikeComment> userLikeComment = userLikeCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userLikeComment);
    }

    /**
     * {@code DELETE  /user-like-comments/:id} : delete the "id" userLikeComment.
     *
     * @param id the id of the userLikeComment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-like-comments/{id}")
    public ResponseEntity<Void> deleteUserLikeComment(@PathVariable Long id) {
        log.debug("REST request to delete UserLikeComment : {}", id);
        userLikeCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
