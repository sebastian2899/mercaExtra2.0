package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoPromocionHomeMapperTest {

    private ProductoPromocionHomeMapper productoPromocionHomeMapper;

    @BeforeEach
    public void setUp() {
        productoPromocionHomeMapper = new ProductoPromocionHomeMapperImpl();
    }
}
