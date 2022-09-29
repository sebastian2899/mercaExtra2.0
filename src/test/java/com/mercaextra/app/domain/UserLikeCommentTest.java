package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLikeCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLikeComment.class);
        UserLikeComment userLikeComment1 = new UserLikeComment();
        userLikeComment1.setId(1L);
        UserLikeComment userLikeComment2 = new UserLikeComment();
        userLikeComment2.setId(userLikeComment1.getId());
        assertThat(userLikeComment1).isEqualTo(userLikeComment2);
        userLikeComment2.setId(2L);
        assertThat(userLikeComment1).isNotEqualTo(userLikeComment2);
        userLikeComment1.setId(null);
        assertThat(userLikeComment1).isNotEqualTo(userLikeComment2);
    }
}
