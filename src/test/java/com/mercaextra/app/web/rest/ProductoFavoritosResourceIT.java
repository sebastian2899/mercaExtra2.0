package com.mercaextra.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.ProductoFavoritos;
import com.mercaextra.app.repository.ProductoFavoritosRepository;
import com.mercaextra.app.service.criteria.ProductoFavoritosCriteria;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import com.mercaextra.app.service.mapper.ProductoFavoritosMapper;
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
 * Integration tests for the {@link ProductoFavoritosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoFavoritosResourceIT {

    private static final Long DEFAULT_ID_PRODUCT = 1L;
    private static final Long UPDATED_ID_PRODUCT = 2L;
    private static final Long SMALLER_ID_PRODUCT = 1L - 1L;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/producto-favoritos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoFavoritosRepository productoFavoritosRepository;

    @Autowired
    private ProductoFavoritosMapper productoFavoritosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoFavoritosMockMvc;

    private ProductoFavoritos productoFavoritos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoFavoritos createEntity(EntityManager em) {
        ProductoFavoritos productoFavoritos = new ProductoFavoritos()
            .idProduct(DEFAULT_ID_PRODUCT)
            .login(DEFAULT_LOGIN)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .estado(DEFAULT_ESTADO);
        return productoFavoritos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoFavoritos createUpdatedEntity(EntityManager em) {
        ProductoFavoritos productoFavoritos = new ProductoFavoritos()
            .idProduct(UPDATED_ID_PRODUCT)
            .login(UPDATED_LOGIN)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .estado(UPDATED_ESTADO);
        return productoFavoritos;
    }

    @BeforeEach
    public void initTest() {
        productoFavoritos = createEntity(em);
    }

    @Test
    @Transactional
    void createProductoFavoritos() throws Exception {
        int databaseSizeBeforeCreate = productoFavoritosRepository.findAll().size();
        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);
        restProductoFavoritosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoFavoritos testProductoFavoritos = productoFavoritosList.get(productoFavoritosList.size() - 1);
        assertThat(testProductoFavoritos.getIdProduct()).isEqualTo(DEFAULT_ID_PRODUCT);
        assertThat(testProductoFavoritos.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testProductoFavoritos.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testProductoFavoritos.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createProductoFavoritosWithExistingId() throws Exception {
        // Create the ProductoFavoritos with an existing ID
        productoFavoritos.setId(1L);
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        int databaseSizeBeforeCreate = productoFavoritosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoFavoritosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductoFavoritos() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoFavoritos.getId().intValue())))
            .andExpect(jsonPath("$.[*].idProduct").value(hasItem(DEFAULT_ID_PRODUCT.intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getProductoFavoritos() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get the productoFavoritos
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL_ID, productoFavoritos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productoFavoritos.getId().intValue()))
            .andExpect(jsonPath("$.idProduct").value(DEFAULT_ID_PRODUCT.intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getProductoFavoritosByIdFiltering() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        Long id = productoFavoritos.getId();

        defaultProductoFavoritosShouldBeFound("id.equals=" + id);
        defaultProductoFavoritosShouldNotBeFound("id.notEquals=" + id);

        defaultProductoFavoritosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoFavoritosShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoFavoritosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoFavoritosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct equals to DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.equals=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct equals to UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.equals=" + UPDATED_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct not equals to DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.notEquals=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct not equals to UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.notEquals=" + UPDATED_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsInShouldWork() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct in DEFAULT_ID_PRODUCT or UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.in=" + DEFAULT_ID_PRODUCT + "," + UPDATED_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct equals to UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.in=" + UPDATED_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct is not null
        defaultProductoFavoritosShouldBeFound("idProduct.specified=true");

        // Get all the productoFavoritosList where idProduct is null
        defaultProductoFavoritosShouldNotBeFound("idProduct.specified=false");
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct is greater than or equal to DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.greaterThanOrEqual=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct is greater than or equal to UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.greaterThanOrEqual=" + UPDATED_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct is less than or equal to DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.lessThanOrEqual=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct is less than or equal to SMALLER_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.lessThanOrEqual=" + SMALLER_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsLessThanSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct is less than DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.lessThan=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct is less than UPDATED_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.lessThan=" + UPDATED_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByIdProductIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where idProduct is greater than DEFAULT_ID_PRODUCT
        defaultProductoFavoritosShouldNotBeFound("idProduct.greaterThan=" + DEFAULT_ID_PRODUCT);

        // Get all the productoFavoritosList where idProduct is greater than SMALLER_ID_PRODUCT
        defaultProductoFavoritosShouldBeFound("idProduct.greaterThan=" + SMALLER_ID_PRODUCT);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login equals to DEFAULT_LOGIN
        defaultProductoFavoritosShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the productoFavoritosList where login equals to UPDATED_LOGIN
        defaultProductoFavoritosShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login not equals to DEFAULT_LOGIN
        defaultProductoFavoritosShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the productoFavoritosList where login not equals to UPDATED_LOGIN
        defaultProductoFavoritosShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultProductoFavoritosShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the productoFavoritosList where login equals to UPDATED_LOGIN
        defaultProductoFavoritosShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login is not null
        defaultProductoFavoritosShouldBeFound("login.specified=true");

        // Get all the productoFavoritosList where login is null
        defaultProductoFavoritosShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginContainsSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login contains DEFAULT_LOGIN
        defaultProductoFavoritosShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the productoFavoritosList where login contains UPDATED_LOGIN
        defaultProductoFavoritosShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where login does not contain DEFAULT_LOGIN
        defaultProductoFavoritosShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the productoFavoritosList where login does not contain UPDATED_LOGIN
        defaultProductoFavoritosShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLastUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where lastUpdate equals to DEFAULT_LAST_UPDATE
        defaultProductoFavoritosShouldBeFound("lastUpdate.equals=" + DEFAULT_LAST_UPDATE);

        // Get all the productoFavoritosList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultProductoFavoritosShouldNotBeFound("lastUpdate.equals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLastUpdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where lastUpdate not equals to DEFAULT_LAST_UPDATE
        defaultProductoFavoritosShouldNotBeFound("lastUpdate.notEquals=" + DEFAULT_LAST_UPDATE);

        // Get all the productoFavoritosList where lastUpdate not equals to UPDATED_LAST_UPDATE
        defaultProductoFavoritosShouldBeFound("lastUpdate.notEquals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLastUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where lastUpdate in DEFAULT_LAST_UPDATE or UPDATED_LAST_UPDATE
        defaultProductoFavoritosShouldBeFound("lastUpdate.in=" + DEFAULT_LAST_UPDATE + "," + UPDATED_LAST_UPDATE);

        // Get all the productoFavoritosList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultProductoFavoritosShouldNotBeFound("lastUpdate.in=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByLastUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where lastUpdate is not null
        defaultProductoFavoritosShouldBeFound("lastUpdate.specified=true");

        // Get all the productoFavoritosList where lastUpdate is null
        defaultProductoFavoritosShouldNotBeFound("lastUpdate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado equals to DEFAULT_ESTADO
        defaultProductoFavoritosShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the productoFavoritosList where estado equals to UPDATED_ESTADO
        defaultProductoFavoritosShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado not equals to DEFAULT_ESTADO
        defaultProductoFavoritosShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the productoFavoritosList where estado not equals to UPDATED_ESTADO
        defaultProductoFavoritosShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultProductoFavoritosShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the productoFavoritosList where estado equals to UPDATED_ESTADO
        defaultProductoFavoritosShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado is not null
        defaultProductoFavoritosShouldBeFound("estado.specified=true");

        // Get all the productoFavoritosList where estado is null
        defaultProductoFavoritosShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado contains DEFAULT_ESTADO
        defaultProductoFavoritosShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the productoFavoritosList where estado contains UPDATED_ESTADO
        defaultProductoFavoritosShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllProductoFavoritosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        // Get all the productoFavoritosList where estado does not contain DEFAULT_ESTADO
        defaultProductoFavoritosShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the productoFavoritosList where estado does not contain UPDATED_ESTADO
        defaultProductoFavoritosShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoFavoritosShouldBeFound(String filter) throws Exception {
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoFavoritos.getId().intValue())))
            .andExpect(jsonPath("$.[*].idProduct").value(hasItem(DEFAULT_ID_PRODUCT.intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoFavoritosShouldNotBeFound(String filter) throws Exception {
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoFavoritosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductoFavoritos() throws Exception {
        // Get the productoFavoritos
        restProductoFavoritosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductoFavoritos() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();

        // Update the productoFavoritos
        ProductoFavoritos updatedProductoFavoritos = productoFavoritosRepository.findById(productoFavoritos.getId()).get();
        // Disconnect from session so that the updates on updatedProductoFavoritos are not directly saved in db
        em.detach(updatedProductoFavoritos);
        updatedProductoFavoritos.idProduct(UPDATED_ID_PRODUCT).login(UPDATED_LOGIN).lastUpdate(UPDATED_LAST_UPDATE).estado(UPDATED_ESTADO);
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(updatedProductoFavoritos);

        restProductoFavoritosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoFavoritosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
        ProductoFavoritos testProductoFavoritos = productoFavoritosList.get(productoFavoritosList.size() - 1);
        assertThat(testProductoFavoritos.getIdProduct()).isEqualTo(UPDATED_ID_PRODUCT);
        assertThat(testProductoFavoritos.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testProductoFavoritos.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testProductoFavoritos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoFavoritosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoFavoritosWithPatch() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();

        // Update the productoFavoritos using partial update
        ProductoFavoritos partialUpdatedProductoFavoritos = new ProductoFavoritos();
        partialUpdatedProductoFavoritos.setId(productoFavoritos.getId());

        partialUpdatedProductoFavoritos.lastUpdate(UPDATED_LAST_UPDATE).estado(UPDATED_ESTADO);

        restProductoFavoritosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoFavoritos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoFavoritos))
            )
            .andExpect(status().isOk());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
        ProductoFavoritos testProductoFavoritos = productoFavoritosList.get(productoFavoritosList.size() - 1);
        assertThat(testProductoFavoritos.getIdProduct()).isEqualTo(DEFAULT_ID_PRODUCT);
        assertThat(testProductoFavoritos.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testProductoFavoritos.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testProductoFavoritos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateProductoFavoritosWithPatch() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();

        // Update the productoFavoritos using partial update
        ProductoFavoritos partialUpdatedProductoFavoritos = new ProductoFavoritos();
        partialUpdatedProductoFavoritos.setId(productoFavoritos.getId());

        partialUpdatedProductoFavoritos
            .idProduct(UPDATED_ID_PRODUCT)
            .login(UPDATED_LOGIN)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .estado(UPDATED_ESTADO);

        restProductoFavoritosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoFavoritos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoFavoritos))
            )
            .andExpect(status().isOk());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
        ProductoFavoritos testProductoFavoritos = productoFavoritosList.get(productoFavoritosList.size() - 1);
        assertThat(testProductoFavoritos.getIdProduct()).isEqualTo(UPDATED_ID_PRODUCT);
        assertThat(testProductoFavoritos.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testProductoFavoritos.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testProductoFavoritos.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoFavoritosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductoFavoritos() throws Exception {
        int databaseSizeBeforeUpdate = productoFavoritosRepository.findAll().size();
        productoFavoritos.setId(count.incrementAndGet());

        // Create the ProductoFavoritos
        ProductoFavoritosDTO productoFavoritosDTO = productoFavoritosMapper.toDto(productoFavoritos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoFavoritosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoFavoritosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoFavoritos in the database
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductoFavoritos() throws Exception {
        // Initialize the database
        productoFavoritosRepository.saveAndFlush(productoFavoritos);

        int databaseSizeBeforeDelete = productoFavoritosRepository.findAll().size();

        // Delete the productoFavoritos
        restProductoFavoritosMockMvc
            .perform(delete(ENTITY_API_URL_ID, productoFavoritos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoFavoritos> productoFavoritosList = productoFavoritosRepository.findAll();
        assertThat(productoFavoritosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
