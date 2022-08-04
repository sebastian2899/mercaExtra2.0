package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.ProductoPromocionHome} entity.
 */
public class ProductoPromocionHomeDTO implements Serializable {

    private Long id;

    private Long idProducto;

    private String descripcion;

    private Instant fechaAgregado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(Instant fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoPromocionHomeDTO)) {
            return false;
        }

        ProductoPromocionHomeDTO productoPromocionHomeDTO = (ProductoPromocionHomeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productoPromocionHomeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoPromocionHomeDTO{" +
            "id=" + getId() +
            ", idProducto=" + getIdProducto() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaAgregado='" + getFechaAgregado() + "'" +
            "}";
    }
}
