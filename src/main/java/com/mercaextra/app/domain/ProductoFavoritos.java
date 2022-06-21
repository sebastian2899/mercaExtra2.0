package com.mercaextra.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductoFavoritos.
 */
@Entity
@Table(name = "producto_favoritos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductoFavoritos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_product")
    private Long idProduct;

    @Column(name = "login")
    private String login;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "estado")
    private String estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductoFavoritos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduct() {
        return this.idProduct;
    }

    public ProductoFavoritos idProduct(Long idProduct) {
        this.setIdProduct(idProduct);
        return this;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getLogin() {
        return this.login;
    }

    public ProductoFavoritos login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public ProductoFavoritos lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getEstado() {
        return this.estado;
    }

    public ProductoFavoritos estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoFavoritos)) {
            return false;
        }
        return id != null && id.equals(((ProductoFavoritos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoFavoritos{" +
            "id=" + getId() +
            ", idProduct=" + getIdProduct() +
            ", login='" + getLogin() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
