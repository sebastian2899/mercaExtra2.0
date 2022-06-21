package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoFavoritosMapperTest {

    private ProductoFavoritosMapper productoFavoritosMapper;

    @BeforeEach
    public void setUp() {
        productoFavoritosMapper = new ProductoFavoritosMapperImpl();
    }
}
