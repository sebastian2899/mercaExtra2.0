package com.mercaextra.app.service;

import com.mercaextra.app.domain.*; // for static metamodels
import com.mercaextra.app.domain.ProductoFavoritos;
import com.mercaextra.app.repository.ProductoFavoritosRepository;
import com.mercaextra.app.service.criteria.ProductoFavoritosCriteria;
import com.mercaextra.app.service.dto.ProductoFavoritosDTO;
import com.mercaextra.app.service.mapper.ProductoFavoritosMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductoFavoritos} entities in the database.
 * The main input is a {@link ProductoFavoritosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductoFavoritosDTO} or a {@link Page} of {@link ProductoFavoritosDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoFavoritosQueryService extends QueryService<ProductoFavoritos> {

    private final Logger log = LoggerFactory.getLogger(ProductoFavoritosQueryService.class);

    private final ProductoFavoritosRepository productoFavoritosRepository;

    private final ProductoFavoritosMapper productoFavoritosMapper;

    public ProductoFavoritosQueryService(
        ProductoFavoritosRepository productoFavoritosRepository,
        ProductoFavoritosMapper productoFavoritosMapper
    ) {
        this.productoFavoritosRepository = productoFavoritosRepository;
        this.productoFavoritosMapper = productoFavoritosMapper;
    }

    /**
     * Return a {@link List} of {@link ProductoFavoritosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductoFavoritosDTO> findByCriteria(ProductoFavoritosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductoFavoritos> specification = createSpecification(criteria);
        return productoFavoritosMapper.toDto(productoFavoritosRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductoFavoritosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoFavoritosDTO> findByCriteria(ProductoFavoritosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductoFavoritos> specification = createSpecification(criteria);
        return productoFavoritosRepository.findAll(specification, page).map(productoFavoritosMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoFavoritosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductoFavoritos> specification = createSpecification(criteria);
        return productoFavoritosRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoFavoritosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductoFavoritos> createSpecification(ProductoFavoritosCriteria criteria) {
        Specification<ProductoFavoritos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductoFavoritos_.id));
            }
            if (criteria.getIdProduct() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdProduct(), ProductoFavoritos_.idProduct));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), ProductoFavoritos_.login));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), ProductoFavoritos_.lastUpdate));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), ProductoFavoritos_.estado));
            }
        }
        return specification;
    }
}
