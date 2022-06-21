package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.ProductoFavoritos;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductoFavoritos} and its DTO {@link ProductoFavoritosDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoFavoritosMapper extends EntityMapper<ProductoFavoritosDTO, ProductoFavoritos> {}
