package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductoPromocionHomeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoPromocionHomeDTO.class);
        ProductoPromocionHomeDTO productoPromocionHomeDTO1 = new ProductoPromocionHomeDTO();
        productoPromocionHomeDTO1.setId(1L);
        ProductoPromocionHomeDTO productoPromocionHomeDTO2 = new ProductoPromocionHomeDTO();
        assertThat(productoPromocionHomeDTO1).isNotEqualTo(productoPromocionHomeDTO2);
        productoPromocionHomeDTO2.setId(productoPromocionHomeDTO1.getId());
        assertThat(productoPromocionHomeDTO1).isEqualTo(productoPromocionHomeDTO2);
        productoPromocionHomeDTO2.setId(2L);
        assertThat(productoPromocionHomeDTO1).isNotEqualTo(productoPromocionHomeDTO2);
        productoPromocionHomeDTO1.setId(null);
        assertThat(productoPromocionHomeDTO1).isNotEqualTo(productoPromocionHomeDTO2);
    }
}
