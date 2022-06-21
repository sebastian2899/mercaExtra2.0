package com.mercaextra.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mercaextra.app.domain.ProductoFavoritos} entity. This class is used
 * in {@link com.mercaextra.app.web.rest.ProductoFavoritosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /producto-favoritos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductoFavoritosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter idProduct;

    private StringFilter login;

    private InstantFilter lastUpdate;

    private StringFilter estado;

    private Boolean distinct;

    public ProductoFavoritosCriteria() {}

    public ProductoFavoritosCriteria(ProductoFavoritosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idProduct = other.idProduct == null ? null : other.idProduct.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.lastUpdate = other.lastUpdate == null ? null : other.lastUpdate.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductoFavoritosCriteria copy() {
        return new ProductoFavoritosCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getIdProduct() {
        return idProduct;
    }

    public LongFilter idProduct() {
        if (idProduct == null) {
            idProduct = new LongFilter();
        }
        return idProduct;
    }

    public void setIdProduct(LongFilter idProduct) {
        this.idProduct = idProduct;
    }

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public InstantFilter getLastUpdate() {
        return lastUpdate;
    }

    public InstantFilter lastUpdate() {
        if (lastUpdate == null) {
            lastUpdate = new InstantFilter();
        }
        return lastUpdate;
    }

    public void setLastUpdate(InstantFilter lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public StringFilter estado() {
        if (estado == null) {
            estado = new StringFilter();
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoFavoritosCriteria that = (ProductoFavoritosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idProduct, that.idProduct) &&
            Objects.equals(login, that.login) &&
            Objects.equals(lastUpdate, that.lastUpdate) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProduct, login, lastUpdate, estado, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoFavoritosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idProduct != null ? "idProduct=" + idProduct + ", " : "") +
            (login != null ? "login=" + login + ", " : "") +
            (lastUpdate != null ? "lastUpdate=" + lastUpdate + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
