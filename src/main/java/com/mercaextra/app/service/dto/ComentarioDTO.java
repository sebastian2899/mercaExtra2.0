package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Comentario} entity.
 */
public class ComentarioDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long idComentario;

    private Instant fechaComentario;

    private String login;

    private Long likes;

    private String descripcion;

    private Long idProducto;

    private List<ComentarioDTO> comentariosRespuesta;

    public List<ComentarioDTO> getComentariosRespuesta() {
        return comentariosRespuesta;
    }

    public void setComentariosRespuesta(List<ComentarioDTO> comentariosRespuesta) {
        this.comentariosRespuesta = comentariosRespuesta;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Instant getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(Instant fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComentarioDTO)) {
            return false;
        }

        ComentarioDTO comentarioDTO = (ComentarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, comentarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComentarioDTO{" +
            "id=" + getId() +
            ", idComentario=" + getIdComentario() +
            ", fechaComentario='" + getFechaComentario() + "'" +
            ", login='" + getLogin() + "'" +
            ", like=" + getLikes() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
