package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.ProductoPromocionHomeRepository;
import com.mercaextra.app.service.ProductoPromocionHomeService;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoPromocionHomeDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.ProductoPromocionHome}.
 */
@RestController
@RequestMapping("/api")
public class ProductoPromocionHomeResource {

    private final Logger log = LoggerFactory.getLogger(ProductoPromocionHomeResource.class);

    private static final String ENTITY_NAME = "productoPromocionHome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoPromocionHomeService productoPromocionHomeService;

    private final ProductoPromocionHomeRepository productoPromocionHomeRepository;

    public ProductoPromocionHomeResource(
        ProductoPromocionHomeService productoPromocionHomeService,
        ProductoPromocionHomeRepository productoPromocionHomeRepository
    ) {
        this.productoPromocionHomeService = productoPromocionHomeService;
        this.productoPromocionHomeRepository = productoPromocionHomeRepository;
    }

    /**
     * {@code POST  /producto-promocion-homes} : Create a new productoPromocionHome.
     *
     * @param productoPromocionHomeDTO the productoPromocionHomeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoPromocionHomeDTO, or with status {@code 400 (Bad Request)} if the productoPromocionHome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/producto-promocion-homes")
    public ResponseEntity<ProductoPromocionHomeDTO> createProductoPromocionHome(
        @RequestBody ProductoPromocionHomeDTO productoPromocionHomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProductoPromocionHome : {}", productoPromocionHomeDTO);
        if (productoPromocionHomeDTO.getId() != null) {
            throw new BadRequestAlertException("A new productoPromocionHome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoPromocionHomeDTO result = productoPromocionHomeService.save(productoPromocionHomeDTO);
        return ResponseEntity
            .created(new URI("/api/producto-promocion-homes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /producto-promocion-homes/:id} : Updates an existing productoPromocionHome.
     *
     * @param id the id of the productoPromocionHomeDTO to save.
     * @param productoPromocionHomeDTO the productoPromocionHomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoPromocionHomeDTO,
     * or with status {@code 400 (Bad Request)} if the productoPromocionHomeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productoPromocionHomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/producto-promocion-homes/{id}")
    public ResponseEntity<ProductoPromocionHomeDTO> updateProductoPromocionHome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductoPromocionHomeDTO productoPromocionHomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductoPromocionHome : {}, {}", id, productoPromocionHomeDTO);
        if (productoPromocionHomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoPromocionHomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoPromocionHomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductoPromocionHomeDTO result = productoPromocionHomeService.save(productoPromocionHomeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoPromocionHomeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /producto-promocion-homes/:id} : Partial updates given fields of an existing productoPromocionHome, field will ignore if it is null
     *
     * @param id the id of the productoPromocionHomeDTO to save.
     * @param productoPromocionHomeDTO the productoPromocionHomeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoPromocionHomeDTO,
     * or with status {@code 400 (Bad Request)} if the productoPromocionHomeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productoPromocionHomeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productoPromocionHomeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/producto-promocion-homes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductoPromocionHomeDTO> partialUpdateProductoPromocionHome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductoPromocionHomeDTO productoPromocionHomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductoPromocionHome partially : {}, {}", id, productoPromocionHomeDTO);
        if (productoPromocionHomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoPromocionHomeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoPromocionHomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductoPromocionHomeDTO> result = productoPromocionHomeService.partialUpdate(productoPromocionHomeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoPromocionHomeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /producto-promocion-homes} : get all the productoPromocionHomes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productoPromocionHomes in body.
     */
    @GetMapping("/producto-promocion-homes")
    public List<ProductoPromocionHomeDTO> getAllProductoPromocionHomes() {
        log.debug("REST request to get all ProductoPromocionHomes");
        return productoPromocionHomeService.findAll();
    }

    /**
     * {@code GET  /producto-promocion-homes/:id} : get the "id" productoPromocionHome.
     *
     * @param id the id of the productoPromocionHomeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoPromocionHomeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/producto-promocion-homes/{id}")
    public ResponseEntity<ProductoPromocionHomeDTO> getProductoPromocionHome(@PathVariable Long id) {
        log.debug("REST request to get ProductoPromocionHome : {}", id);
        Optional<ProductoPromocionHomeDTO> productoPromocionHomeDTO = productoPromocionHomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productoPromocionHomeDTO);
    }

    @GetMapping("/producto-home-all-disscount")
    public List<ProductoDTO> productosDescuento() {
        log.debug("Rest request to get all products whit disscound");
        return productoPromocionHomeService.productosDescuento();
    }

    /**
     * {@code DELETE  /producto-promocion-homes/:id} : delete the "id" productoPromocionHome.
     *
     * @param id the id of the productoPromocionHomeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/producto-promocion-homes/{id}")
    public ResponseEntity<Void> deleteProductoPromocionHome(@PathVariable Long id) {
        log.debug("REST request to delete ProductoPromocionHome : {}", id);
        productoPromocionHomeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
