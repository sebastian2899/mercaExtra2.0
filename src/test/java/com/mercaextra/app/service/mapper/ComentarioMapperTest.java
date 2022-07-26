package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComentarioMapperTest {

    private ComentarioMapper comentarioMapper;

    @BeforeEach
    public void setUp() {
        comentarioMapper = new ComentarioMapperImpl();
    }
}
