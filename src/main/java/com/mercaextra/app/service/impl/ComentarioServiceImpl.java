package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Comentario;
import com.mercaextra.app.domain.UserLikeComment;
import com.mercaextra.app.repository.ComentarioRepository;
import com.mercaextra.app.repository.UserLikeCommentRepository;
import com.mercaextra.app.service.ComentarioService;
import com.mercaextra.app.service.UserService;
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

    private final UserService userService;

    private final UserLikeCommentRepository userLikeCommentRepository;

    public ComentarioServiceImpl(
        ComentarioRepository comentarioRepository,
        ComentarioMapper comentarioMapper,
        UserService userService,
        UserLikeCommentRepository userLikeCommentRepository
    ) {
        this.comentarioRepository = comentarioRepository;
        this.comentarioMapper = comentarioMapper;
        this.userService = userService;
        this.userLikeCommentRepository = userLikeCommentRepository;
    }

    @Override
    public ComentarioDTO save(ComentarioDTO comentarioDTO) {
        log.debug("Request to save Comentario : {}", comentarioDTO);
        Comentario comentario = comentarioMapper.toEntity(comentarioDTO);
        comentario.setLikes(comentarioDTO.getLikes() == null ? 0 : comentarioDTO.getLikes());
        UserLikeComment userLike = new UserLikeComment();

        String login = userService.getUserWithAuthorities().get().getLogin();
        comentario.setLogin(login);
        comentario = comentarioRepository.save(comentario);

        userLike.setLogin(login);
        userLike.setIdComment(comentario.getId());
        userLike.setIsLike(false);
        userLikeCommentRepository.save(userLike);

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
    public List<ComentarioDTO> uploadCommentsProduct(Long idProducto) {
        log.debug("Request to get all coments per product");
        return comentarioRepository
            .ComentsProducts(idProducto)
            .stream()
            .map(comentarioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ComentarioDTO> responseComments(Long idProducto, Long idComentario) {
        log.debug("Request to get all coments response per product");
        // SE LE DEBE ASIGNAR LA LISTA DE RESPUESTAS A CADA COMENTARIO, DESDE EL FRONT
        // SE ENVIA EL ID DEL COMENTARIO Y EL ID PRODUCTO PARA OBTENER LAS RESPUESTAS
        // EL FRONT SE ENCARGARA DE CONSULTAR LA LISTA DE COMENTARIO, VALIDANDO 1 POR 1
        // SI EL COMENTARIO TIENE RESPUESTAS O NO
        return comentarioRepository
            .respuestaComentarios(idProducto, idComentario)
            .stream()
            .map(comentarioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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

    @Override
    public void managementLikeComment(ComentarioDTO comentarioDTO) {
        log.info("Start the process to add or substrac likes comment per login");
        Comentario comentarioEntity = comentarioMapper.toEntity(comentarioDTO);
        Long allLikes = comentarioDTO.getLikes();
        UserLikeComment userLikeComment = userLikeCommentRepository.userLikePerCommet(
            userService.getUserWithAuthorities().get().getLogin(),
            comentarioDTO.getId()
        );

        if (userLikeComment == null) {
            UserLikeComment userLikeCommentNew = new UserLikeComment();
            userLikeCommentNew.setIdComment(comentarioDTO.getId());
            userLikeCommentNew.setLogin(userService.getUserWithAuthorities().get().getLogin());
            userLikeCommentNew.setIsLike(true);
            comentarioEntity.setLikes(allLikes.longValue() + 1);
            userLikeCommentRepository.save(userLikeCommentNew);
        } else if (!userLikeComment.getIsLike()) {
            comentarioEntity.setLikes(allLikes.longValue() + 1);
            userLikeComment.setIsLike(true);
        } else {
            comentarioEntity.setLikes(allLikes.longValue() - 1);
            userLikeComment.setIsLike(false);
        }

        comentarioRepository.save(comentarioEntity);
        if (null != userLikeComment) {
            userLikeCommentRepository.save(userLikeComment);
        }
    }
}
