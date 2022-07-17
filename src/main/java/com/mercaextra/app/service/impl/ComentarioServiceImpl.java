package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Comentario;
import com.mercaextra.app.repository.ComentarioRepository;
import com.mercaextra.app.service.ComentarioService;
import com.mercaextra.app.service.dto.ComentarioDTO;
import com.mercaextra.app.service.mapper.ComentarioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comentario}.
 */
@Service
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    private final Logger log = LoggerFactory.getLogger(ComentarioServiceImpl.class);

    private final ComentarioRepository comentarioRepository;

    private final ComentarioMapper comentarioMapper;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, ComentarioMapper comentarioMapper) {
        this.comentarioRepository = comentarioRepository;
        this.comentarioMapper = comentarioMapper;
    }

    @Override
    public ComentarioDTO save(ComentarioDTO comentarioDTO) {
        log.debug("Request to save Comentario : {}", comentarioDTO);
        Comentario comentario = comentarioMapper.toEntity(comentarioDTO);
        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toDto(comentario);
    }

    @Override
    public Optional<ComentarioDTO> partialUpdate(ComentarioDTO comentarioDTO) {
        log.debug("Request to partially update Comentario : {}", comentarioDTO);

        return comentarioRepository
            .findById(comentarioDTO.getId())
            .map(existingComentario -> {
                comentarioMapper.partialUpdate(existingComentario, comentarioDTO);

                return existingComentario;
            })
            .map(comentarioRepository::save)
            .map(comentarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDTO> findAll() {
        log.debug("Request to get all Comentarios");
        return comentarioRepository.findAll().stream().map(comentarioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComentarioDTO> findOne(Long id) {
        log.debug("Request to get Comentario : {}", id);
        return comentarioRepository.findById(id).map(comentarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comentario : {}", id);
        comentarioRepository.deleteById(id);
    }
}
