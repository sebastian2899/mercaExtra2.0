package com.mercaextra.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.UserLikeComment;
import com.mercaextra.app.repository.UserLikeCommentRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserLikeCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserLikeCommentResourceIT {

    private static final Long DEFAULT_ID_COMMENT = 1L;
    private static final Long UPDATED_ID_COMMENT = 2L;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LIKE = false;
    private static final Boolean UPDATED_IS_LIKE = true;

    private static final String ENTITY_API_URL = "/api/user-like-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserLikeCommentRepository userLikeCommentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserLikeCommentMockMvc;

    private UserLikeComment userLikeComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLikeComment createEntity(EntityManager em) {
        UserLikeComment userLikeComment = new UserLikeComment().idComment(DEFAULT_ID_COMMENT).login(DEFAULT_LOGIN).isLike(DEFAULT_IS_LIKE);
        return userLikeComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLikeComment createUpdatedEntity(EntityManager em) {
        UserLikeComment userLikeComment = new UserLikeComment().idComment(UPDATED_ID_COMMENT).login(UPDATED_LOGIN).isLike(UPDATED_IS_LIKE);
        return userLikeComment;
    }

    @BeforeEach
    public void initTest() {
        userLikeComment = createEntity(em);
    }

    @Test
    @Transactional
    void createUserLikeComment() throws Exception {
        int databaseSizeBeforeCreate = userLikeCommentRepository.findAll().size();
        // Create the UserLikeComment
        restUserLikeCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isCreated());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeCreate + 1);
        UserLikeComment testUserLikeComment = userLikeCommentList.get(userLikeCommentList.size() - 1);
        assertThat(testUserLikeComment.getIdComment()).isEqualTo(DEFAULT_ID_COMMENT);
        assertThat(testUserLikeComment.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUserLikeComment.getIsLike()).isEqualTo(DEFAULT_IS_LIKE);
    }

    @Test
    @Transactional
    void createUserLikeCommentWithExistingId() throws Exception {
        // Create the UserLikeComment with an existing ID
        userLikeComment.setId(1L);

        int databaseSizeBeforeCreate = userLikeCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLikeCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserLikeComments() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        // Get all the userLikeCommentList
        restUserLikeCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLikeComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].idComment").value(hasItem(DEFAULT_ID_COMMENT.intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].isLike").value(hasItem(DEFAULT_IS_LIKE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserLikeComment() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        // Get the userLikeComment
        restUserLikeCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, userLikeComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userLikeComment.getId().intValue()))
            .andExpect(jsonPath("$.idComment").value(DEFAULT_ID_COMMENT.intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.isLike").value(DEFAULT_IS_LIKE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserLikeComment() throws Exception {
        // Get the userLikeComment
        restUserLikeCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserLikeComment() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();

        // Update the userLikeComment
        UserLikeComment updatedUserLikeComment = userLikeCommentRepository.findById(userLikeComment.getId()).get();
        // Disconnect from session so that the updates on updatedUserLikeComment are not directly saved in db
        em.detach(updatedUserLikeComment);
        updatedUserLikeComment.idComment(UPDATED_ID_COMMENT).login(UPDATED_LOGIN).isLike(UPDATED_IS_LIKE);

        restUserLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserLikeComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserLikeComment))
            )
            .andExpect(status().isOk());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
        UserLikeComment testUserLikeComment = userLikeCommentList.get(userLikeCommentList.size() - 1);
        assertThat(testUserLikeComment.getIdComment()).isEqualTo(UPDATED_ID_COMMENT);
        assertThat(testUserLikeComment.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUserLikeComment.getIsLike()).isEqualTo(UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    void putNonExistingUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userLikeComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserLikeCommentWithPatch() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();

        // Update the userLikeComment using partial update
        UserLikeComment partialUpdatedUserLikeComment = new UserLikeComment();
        partialUpdatedUserLikeComment.setId(userLikeComment.getId());

        partialUpdatedUserLikeComment.idComment(UPDATED_ID_COMMENT).isLike(UPDATED_IS_LIKE);

        restUserLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLikeComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLikeComment))
            )
            .andExpect(status().isOk());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
        UserLikeComment testUserLikeComment = userLikeCommentList.get(userLikeCommentList.size() - 1);
        assertThat(testUserLikeComment.getIdComment()).isEqualTo(UPDATED_ID_COMMENT);
        assertThat(testUserLikeComment.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUserLikeComment.getIsLike()).isEqualTo(UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    void fullUpdateUserLikeCommentWithPatch() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();

        // Update the userLikeComment using partial update
        UserLikeComment partialUpdatedUserLikeComment = new UserLikeComment();
        partialUpdatedUserLikeComment.setId(userLikeComment.getId());

        partialUpdatedUserLikeComment.idComment(UPDATED_ID_COMMENT).login(UPDATED_LOGIN).isLike(UPDATED_IS_LIKE);

        restUserLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLikeComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLikeComment))
            )
            .andExpect(status().isOk());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
        UserLikeComment testUserLikeComment = userLikeCommentList.get(userLikeCommentList.size() - 1);
        assertThat(testUserLikeComment.getIdComment()).isEqualTo(UPDATED_ID_COMMENT);
        assertThat(testUserLikeComment.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUserLikeComment.getIsLike()).isEqualTo(UPDATED_IS_LIKE);
    }

    @Test
    @Transactional
    void patchNonExistingUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userLikeComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserLikeComment() throws Exception {
        int databaseSizeBeforeUpdate = userLikeCommentRepository.findAll().size();
        userLikeComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLikeCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userLikeComment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLikeComment in the database
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserLikeComment() throws Exception {
        // Initialize the database
        userLikeCommentRepository.saveAndFlush(userLikeComment);

        int databaseSizeBeforeDelete = userLikeCommentRepository.findAll().size();

        // Delete the userLikeComment
        restUserLikeCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, userLikeComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserLikeComment> userLikeCommentList = userLikeCommentRepository.findAll();
        assertThat(userLikeCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
