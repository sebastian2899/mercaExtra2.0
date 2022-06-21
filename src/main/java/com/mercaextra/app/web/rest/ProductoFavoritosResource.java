package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.ProductoFavoritosRepository;
import com.mercaextra.app.service.ProductoFavoritosQueryService;
import com.mercaextra.app.service.ProductoFavoritosService;
import com.mercaextra.app.service.criteria.ProductoFavoritosCriteria;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing {@link com.mercaextra.app.domain.ProductoFavoritos}.
 */
@RestController
@RequestMapping("/api")
public class ProductoFavoritosResource {

    private final Logger log = LoggerFactory.getLogger(ProductoFavoritosResource.class);

    private static final String ENTITY_NAME = "productoFavoritos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoFavoritosService productoFavoritosService;

    private final ProductoFavoritosRepository productoFavoritosRepository;

    private final ProductoFavoritosQueryService productoFavoritosQueryService;

    public ProductoFavoritosResource(
        ProductoFavoritosService productoFavoritosService,
        ProductoFavoritosRepository productoFavoritosRepository,
        ProductoFavoritosQueryService productoFavoritosQueryService
    ) {
        this.productoFavoritosService = productoFavoritosService;
        this.productoFavoritosRepository = productoFavoritosRepository;
        this.productoFavoritosQueryService = productoFavoritosQueryService;
    }

    /**
     * {@code POST  /producto-favoritos} : Create a new productoFavoritos.
     *
     * @param productoFavoritosDTO the productoFavoritosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoFavoritosDTO, or with status {@code 400 (Bad Request)} if the productoFavoritos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/producto-favoritos")
    public ResponseEntity<ProductoFavoritosDTO> createProductoFavoritos(@RequestBody ProductoFavoritosDTO productoFavoritosDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductoFavoritos : {}", productoFavoritosDTO);
        if (productoFavoritosDTO.getId() != null) {
            throw new BadRequestAlertException("A new productoFavoritos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoFavoritosDTO result = productoFavoritosService.save(productoFavoritosDTO);
        return ResponseEntity
            .created(new URI("/api/producto-favoritos/" + result.getId()))
            //.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /producto-favoritos/:id} : Updates an existing productoFavoritos.
     *
     * @param id the id of the productoFavoritosDTO to save.
     * @param productoFavoritosDTO the productoFavoritosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoFavoritosDTO,
     * or with status {@code 400 (Bad Request)} if the productoFavoritosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productoFavoritosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/producto-favoritos/{id}")
    public ResponseEntity<ProductoFavoritosDTO> updateProductoFavoritos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductoFavoritosDTO productoFavoritosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductoFavoritos : {}, {}", id, productoFavoritosDTO);
        if (productoFavoritosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoFavoritosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoFavoritosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductoFavoritosDTO result = productoFavoritosService.save(productoFavoritosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoFavoritosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /producto-favoritos/:id} : Partial updates given fields of an existing productoFavoritos, field will ignore if it is null
     *
     * @param id the id of the productoFavoritosDTO to save.
     * @param productoFavoritosDTO the productoFavoritosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoFavoritosDTO,
     * or with status {@code 400 (Bad Request)} if the productoFavoritosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productoFavoritosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productoFavoritosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/producto-favoritos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductoFavoritosDTO> partialUpdateProductoFavoritos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductoFavoritosDTO productoFavoritosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductoFavoritos partially : {}, {}", id, productoFavoritosDTO);
        if (productoFavoritosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoFavoritosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoFavoritosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductoFavoritosDTO> result = productoFavoritosService.partialUpdate(productoFavoritosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoFavoritosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /producto-favoritos} : get all the productoFavoritos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productoFavoritos in body.
     */
    @GetMapping("/producto-favoritos")
    public ResponseEntity<List<ProductoFavoritosDTO>> getAllProductoFavoritos(ProductoFavoritosCriteria criteria) {
        log.debug("REST request to get ProductoFavoritos by criteria: {}", criteria);
        List<ProductoFavoritosDTO> entityList = productoFavoritosQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /producto-favoritos/count} : count all the productoFavoritos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/producto-favoritos/count")
    public ResponseEntity<Long> countProductoFavoritos(ProductoFavoritosCriteria criteria) {
        log.debug("REST request to count ProductoFavoritos by criteria: {}", criteria);
        return ResponseEntity.ok().body(productoFavoritosQueryService.countByCriteria(criteria));
    }

    @GetMapping("/producto-favoritos-login")
    public ResponseEntity<List<ProductoDTO>> favoriteProductsByLogin() {
        log.debug("REST request toi get all favorite products by login");
        List<ProductoDTO> favorites = productoFavoritosService.favoriteProducts();
        return new ResponseEntity<List<ProductoDTO>>(favorites, HttpStatus.ACCEPTED);
    }

    /**
     * {@code GET  /producto-favoritos/:id} : get the "id" productoFavoritos.
     *
     * @param id the id of the productoFavoritosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoFavoritosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/producto-favoritos/{id}")
    public ResponseEntity<ProductoFavoritosDTO> getProductoFavoritos(@PathVariable Long id) {
        log.debug("REST request to get ProductoFavoritos : {}", id);
        Optional<ProductoFavoritosDTO> productoFavoritosDTO = productoFavoritosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productoFavoritosDTO);
    }

    /**
     * {@code DELETE  /producto-favoritos/:id} : delete the "id" productoFavoritos.
     *
     * @param id the id of the productoFavoritosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/producto-favoritos/{id}")
    public ResponseEntity<Void> deleteProductoFavoritos(@PathVariable Long id) {
        log.debug("REST request to delete ProductoFavoritos : {}", id);
        productoFavoritosService.delete(id);
        return ResponseEntity
            .noContent()
            //.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
