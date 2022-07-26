package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Comentario;
import com.mercaextra.app.service.dto.ComentarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comentario} and its DTO {@link ComentarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ComentarioMapper extends EntityMapper<ComentarioDTO, Comentario> {}
