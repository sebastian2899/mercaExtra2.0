package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductoPromocionHomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoPromocionHome.class);
        ProductoPromocionHome productoPromocionHome1 = new ProductoPromocionHome();
        productoPromocionHome1.setId(1L);
        ProductoPromocionHome productoPromocionHome2 = new ProductoPromocionHome();
        productoPromocionHome2.setId(productoPromocionHome1.getId());
        assertThat(productoPromocionHome1).isEqualTo(productoPromocionHome2);
        productoPromocionHome2.setId(2L);
        assertThat(productoPromocionHome1).isNotEqualTo(productoPromocionHome2);
        productoPromocionHome1.setId(null);
        assertThat(productoPromocionHome1).isNotEqualTo(productoPromocionHome2);
    }
}
