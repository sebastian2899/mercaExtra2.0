package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.ComentarioRepository;
import com.mercaextra.app.service.ComentarioService;
import com.mercaextra.app.service.dto.ComentarioDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.Comentario}.
 */
@RestController
@RequestMapping("/api")
public class ComentarioResource {

    private final Logger log = LoggerFactory.getLogger(ComentarioResource.class);

    private static final String ENTITY_NAME = "comentario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComentarioService comentarioService;

    private final ComentarioRepository comentarioRepository;

    public ComentarioResource(ComentarioService comentarioService, ComentarioRepository comentarioRepository) {
        this.comentarioService = comentarioService;
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * {@code POST  /comentarios} : Create a new comentario.
     *
     * @param comentarioDTO the comentarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comentarioDTO, or with status {@code 400 (Bad Request)} if the comentario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioDTO> createComentario(@RequestBody ComentarioDTO comentarioDTO) throws URISyntaxException {
        log.debug("REST request to save Comentario : {}", comentarioDTO);
        if (comentarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new comentario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentarioDTO result = comentarioService.save(comentarioDTO);
        return ResponseEntity
            .created(new URI("/api/comentarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comentarios/:id} : Updates an existing comentario.
     *
     * @param id the id of the comentarioDTO to save.
     * @param comentarioDTO the comentarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentarioDTO,
     * or with status {@code 400 (Bad Request)} if the comentarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comentarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comentarios/{id}")
    public ResponseEntity<ComentarioDTO> updateComentario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComentarioDTO comentarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Comentario : {}, {}", id, comentarioDTO);
        if (comentarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComentarioDTO result = comentarioService.save(comentarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comentarioDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/comentariosRespuesta/{idProducto}/{idComentario}")
    public List<ComentarioDTO> comentarioRespuesta(@PathVariable Long idProducto, @PathVariable Long idComentario) {
        log.debug("Rest request to get comentarioRespuesta : {}", idProducto, idComentario);
        return comentarioService.responseComments(idProducto, idComentario);
    }

    /**
     * {@code PATCH  /comentarios/:id} : Partial updates given fields of an existing comentario, field will ignore if it is null
     *
     * @param id the id of the comentarioDTO to save.
     * @param comentarioDTO the comentarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentarioDTO,
     * or with status {@code 400 (Bad Request)} if the comentarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the comentarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the comentarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comentarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComentarioDTO> partialUpdateComentario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComentarioDTO comentarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comentario partially : {}, {}", id, comentarioDTO);
        if (comentarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComentarioDTO> result = comentarioService.partialUpdate(comentarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comentarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comentarios} : get all the comentarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comentarios in body.
     */
    @GetMapping("/comentarios")
    public List<ComentarioDTO> getAllComentarios() {
        log.debug("REST request to get all Comentarios");
        return comentarioService.findAll();
    }

    @GetMapping("/comentarioProductos/{idProducto}")
    public List<ComentarioDTO> uploadCommentsProduct(@PathVariable Long idProducto) {
        log.debug("REST request to get all coments per product");
        return comentarioService.uploadCommentsProduct(idProducto);
    }

    @PostMapping("/managementLikesComment")
    public ResponseEntity<Void> managementLikes(@RequestBody ComentarioDTO comentarioDTO) {
        log.info("REST request to managament likes per comment per user login");
        comentarioService.managementLikeComment(comentarioDTO);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, comentarioDTO.getId().toString()))
            .build();
    }

    /**
     * {@code GET  /comentarios/:id} : get the "id" comentario.
     *
     * @param id the id of the comentarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comentarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comentarios/{id}")
    public ResponseEntity<ComentarioDTO> getComentario(@PathVariable Long id) {
        log.debug("REST request to get Comentario : {}", id);
        Optional<ComentarioDTO> comentarioDTO = comentarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comentarioDTO);
    }

    /**
     * {@code DELETE  /comentarios/:id} : delete the "id" comentario.
     *
     * @param id the id of the comentarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        log.debug("REST request to delete Comentario : {}", id);
        comentarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
