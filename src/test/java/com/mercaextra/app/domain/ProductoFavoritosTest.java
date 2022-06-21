package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductoFavoritosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoFavoritos.class);
        ProductoFavoritos productoFavoritos1 = new ProductoFavoritos();
        productoFavoritos1.setId(1L);
        ProductoFavoritos productoFavoritos2 = new ProductoFavoritos();
        productoFavoritos2.setId(productoFavoritos1.getId());
        assertThat(productoFavoritos1).isEqualTo(productoFavoritos2);
        productoFavoritos2.setId(2L);
        assertThat(productoFavoritos1).isNotEqualTo(productoFavoritos2);
        productoFavoritos1.setId(null);
        assertThat(productoFavoritos1).isNotEqualTo(productoFavoritos2);
    }
}
