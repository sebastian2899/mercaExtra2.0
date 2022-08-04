package com.mercaextra.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductoPromocionHome.
 */
@Entity
@Table(name = "producto_promocion_home")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductoPromocionHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_agregado")
    private Instant fechaAgregado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductoPromocionHome id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProducto() {
        return this.idProducto;
    }

    public ProductoPromocionHome idProducto(Long idProducto) {
        this.setIdProducto(idProducto);
        return this;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public ProductoPromocionHome descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaAgregado() {
        return this.fechaAgregado;
    }

    public ProductoPromocionHome fechaAgregado(Instant fechaAgregado) {
        this.setFechaAgregado(fechaAgregado);
        return this;
    }

    public void setFechaAgregado(Instant fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoPromocionHome)) {
            return false;
        }
        return id != null && id.equals(((ProductoPromocionHome) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoPromocionHome{" +
            "id=" + getId() +
            ", idProducto=" + getIdProducto() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaAgregado='" + getFechaAgregado() + "'" +
            "}";
    }
}
