package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.ProductoPromocionHome;
import com.mercaextra.app.service.dto.ProductoPromocionHomeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductoPromocionHome} and its DTO {@link ProductoPromocionHomeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoPromocionHomeMapper extends EntityMapper<ProductoPromocionHomeDTO, ProductoPromocionHome> {}
