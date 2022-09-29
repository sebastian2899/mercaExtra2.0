package com.mercaextra.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserLikeComment.
 */
@Entity
@Table(name = "user_like_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserLikeComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_comment")
    private Long idComment;

    @Column(name = "login")
    private String login;

    @Column(name = "is_like")
    private Boolean isLike;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserLikeComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdComment() {
        return this.idComment;
    }

    public UserLikeComment idComment(Long idComment) {
        this.setIdComment(idComment);
        return this;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }

    public String getLogin() {
        return this.login;
    }

    public UserLikeComment login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getIsLike() {
        return this.isLike;
    }

    public UserLikeComment isLike(Boolean isLike) {
        this.setIsLike(isLike);
        return this;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLikeComment)) {
            return false;
        }
        return id != null && id.equals(((UserLikeComment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLikeComment{" +
            "id=" + getId() +
            ", idComment=" + getIdComment() +
            ", login='" + getLogin() + "'" +
            ", isLike='" + getIsLike() + "'" +
            "}";
    }
}
