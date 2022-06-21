package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductoFavoritosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoFavoritosDTO.class);
        ProductoFavoritosDTO productoFavoritosDTO1 = new ProductoFavoritosDTO();
        productoFavoritosDTO1.setId(1L);
        ProductoFavoritosDTO productoFavoritosDTO2 = new ProductoFavoritosDTO();
        assertThat(productoFavoritosDTO1).isNotEqualTo(productoFavoritosDTO2);
        productoFavoritosDTO2.setId(productoFavoritosDTO1.getId());
        assertThat(productoFavoritosDTO1).isEqualTo(productoFavoritosDTO2);
        productoFavoritosDTO2.setId(2L);
        assertThat(productoFavoritosDTO1).isNotEqualTo(productoFavoritosDTO2);
        productoFavoritosDTO1.setId(null);
        assertThat(productoFavoritosDTO1).isNotEqualTo(productoFavoritosDTO2);
    }
}
