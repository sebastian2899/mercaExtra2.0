package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.ProductoFavoritos} entity.
 */
public class ProductoFavoritosDTO implements Serializable {

    private Long id;

    private Long idProduct;

    private String login;

    private Instant lastUpdate;

    private String estado;

    private int puesto;

    public Long getId() {
        return id;
    }

    public int getPuesto() {
        return puesto;
    }

    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoFavoritosDTO)) {
            return false;
        }

        ProductoFavoritosDTO productoFavoritosDTO = (ProductoFavoritosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productoFavoritosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoFavoritosDTO{" +
            "id=" + getId() +
            ", idProduct=" + getIdProduct() +
            ", login='" + getLogin() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
