package com.mercaextra.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.ProductoPromocionHome;
import com.mercaextra.app.repository.ProductoPromocionHomeRepository;
import com.mercaextra.app.service.dto.ProductoPromocionHomeDTO;
import com.mercaextra.app.service.mapper.ProductoPromocionHomeMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductoPromocionHomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoPromocionHomeResourceIT {

    private static final Long DEFAULT_ID_PRODUCTO = 1L;
    private static final Long UPDATED_ID_PRODUCTO = 2L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_AGREGADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_AGREGADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/producto-promocion-homes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoPromocionHomeRepository productoPromocionHomeRepository;

    @Autowired
    private ProductoPromocionHomeMapper productoPromocionHomeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoPromocionHomeMockMvc;

    private ProductoPromocionHome productoPromocionHome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoPromocionHome createEntity(EntityManager em) {
        ProductoPromocionHome productoPromocionHome = new ProductoPromocionHome()
            .idProducto(DEFAULT_ID_PRODUCTO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaAgregado(DEFAULT_FECHA_AGREGADO);
        return productoPromocionHome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoPromocionHome createUpdatedEntity(EntityManager em) {
        ProductoPromocionHome productoPromocionHome = new ProductoPromocionHome()
            .idProducto(UPDATED_ID_PRODUCTO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAgregado(UPDATED_FECHA_AGREGADO);
        return productoPromocionHome;
    }

    @BeforeEach
    public void initTest() {
        productoPromocionHome = createEntity(em);
    }

    @Test
    @Transactional
    void createProductoPromocionHome() throws Exception {
        int databaseSizeBeforeCreate = productoPromocionHomeRepository.findAll().size();
        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);
        restProductoPromocionHomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoPromocionHome testProductoPromocionHome = productoPromocionHomeList.get(productoPromocionHomeList.size() - 1);
        assertThat(testProductoPromocionHome.getIdProducto()).isEqualTo(DEFAULT_ID_PRODUCTO);
        assertThat(testProductoPromocionHome.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProductoPromocionHome.getFechaAgregado()).isEqualTo(DEFAULT_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void createProductoPromocionHomeWithExistingId() throws Exception {
        // Create the ProductoPromocionHome with an existing ID
        productoPromocionHome.setId(1L);
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        int databaseSizeBeforeCreate = productoPromocionHomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoPromocionHomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductoPromocionHomes() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        // Get all the productoPromocionHomeList
        restProductoPromocionHomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoPromocionHome.getId().intValue())))
            .andExpect(jsonPath("$.[*].idProducto").value(hasItem(DEFAULT_ID_PRODUCTO.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAgregado").value(hasItem(DEFAULT_FECHA_AGREGADO.toString())));
    }

    @Test
    @Transactional
    void getProductoPromocionHome() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        // Get the productoPromocionHome
        restProductoPromocionHomeMockMvc
            .perform(get(ENTITY_API_URL_ID, productoPromocionHome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productoPromocionHome.getId().intValue()))
            .andExpect(jsonPath("$.idProducto").value(DEFAULT_ID_PRODUCTO.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaAgregado").value(DEFAULT_FECHA_AGREGADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductoPromocionHome() throws Exception {
        // Get the productoPromocionHome
        restProductoPromocionHomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductoPromocionHome() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();

        // Update the productoPromocionHome
        ProductoPromocionHome updatedProductoPromocionHome = productoPromocionHomeRepository.findById(productoPromocionHome.getId()).get();
        // Disconnect from session so that the updates on updatedProductoPromocionHome are not directly saved in db
        em.detach(updatedProductoPromocionHome);
        updatedProductoPromocionHome.idProducto(UPDATED_ID_PRODUCTO).descripcion(UPDATED_DESCRIPCION).fechaAgregado(UPDATED_FECHA_AGREGADO);
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(updatedProductoPromocionHome);

        restProductoPromocionHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoPromocionHomeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
        ProductoPromocionHome testProductoPromocionHome = productoPromocionHomeList.get(productoPromocionHomeList.size() - 1);
        assertThat(testProductoPromocionHome.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testProductoPromocionHome.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProductoPromocionHome.getFechaAgregado()).isEqualTo(UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void putNonExistingProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoPromocionHomeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoPromocionHomeWithPatch() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();

        // Update the productoPromocionHome using partial update
        ProductoPromocionHome partialUpdatedProductoPromocionHome = new ProductoPromocionHome();
        partialUpdatedProductoPromocionHome.setId(productoPromocionHome.getId());

        restProductoPromocionHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoPromocionHome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoPromocionHome))
            )
            .andExpect(status().isOk());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
        ProductoPromocionHome testProductoPromocionHome = productoPromocionHomeList.get(productoPromocionHomeList.size() - 1);
        assertThat(testProductoPromocionHome.getIdProducto()).isEqualTo(DEFAULT_ID_PRODUCTO);
        assertThat(testProductoPromocionHome.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProductoPromocionHome.getFechaAgregado()).isEqualTo(DEFAULT_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void fullUpdateProductoPromocionHomeWithPatch() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();

        // Update the productoPromocionHome using partial update
        ProductoPromocionHome partialUpdatedProductoPromocionHome = new ProductoPromocionHome();
        partialUpdatedProductoPromocionHome.setId(productoPromocionHome.getId());

        partialUpdatedProductoPromocionHome
            .idProducto(UPDATED_ID_PRODUCTO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAgregado(UPDATED_FECHA_AGREGADO);

        restProductoPromocionHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoPromocionHome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoPromocionHome))
            )
            .andExpect(status().isOk());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
        ProductoPromocionHome testProductoPromocionHome = productoPromocionHomeList.get(productoPromocionHomeList.size() - 1);
        assertThat(testProductoPromocionHome.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testProductoPromocionHome.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProductoPromocionHome.getFechaAgregado()).isEqualTo(UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void patchNonExistingProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoPromocionHomeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductoPromocionHome() throws Exception {
        int databaseSizeBeforeUpdate = productoPromocionHomeRepository.findAll().size();
        productoPromocionHome.setId(count.incrementAndGet());

        // Create the ProductoPromocionHome
        ProductoPromocionHomeDTO productoPromocionHomeDTO = productoPromocionHomeMapper.toDto(productoPromocionHome);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoPromocionHomeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoPromocionHomeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoPromocionHome in the database
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductoPromocionHome() throws Exception {
        // Initialize the database
        productoPromocionHomeRepository.saveAndFlush(productoPromocionHome);

        int databaseSizeBeforeDelete = productoPromocionHomeRepository.findAll().size();

        // Delete the productoPromocionHome
        restProductoPromocionHomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productoPromocionHome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoPromocionHome> productoPromocionHomeList = productoPromocionHomeRepository.findAll();
        assertThat(productoPromocionHomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
