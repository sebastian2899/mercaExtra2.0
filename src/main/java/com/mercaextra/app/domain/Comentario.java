package com.mercaextra.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Comentario.
 */
@Entity
@Table(name = "comentario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_comentario")
    private Long idComentario;

    @Column(name = "fecha_comentario")
    private Instant fechaComentario;

    @Column(name = "login")
    private String login;

    @Column(name = "jhi_like")
    private Long like;

    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Comentario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdComentario() {
        return this.idComentario;
    }

    public Comentario idComentario(Long idComentario) {
        this.setIdComentario(idComentario);
        return this;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Instant getFechaComentario() {
        return this.fechaComentario;
    }

    public Comentario fechaComentario(Instant fechaComentario) {
        this.setFechaComentario(fechaComentario);
        return this;
    }

    public void setFechaComentario(Instant fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    public String getLogin() {
        return this.login;
    }

    public Comentario login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getLike() {
        return this.like;
    }

    public Comentario like(Long like) {
        this.setLike(like);
        return this;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Comentario descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comentario)) {
            return false;
        }
        return id != null && id.equals(((Comentario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comentario{" +
            "id=" + getId() +
            ", idComentario=" + getIdComentario() +
            ", fechaComentario='" + getFechaComentario() + "'" +
            ", login='" + getLogin() + "'" +
            ", like=" + getLike() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
