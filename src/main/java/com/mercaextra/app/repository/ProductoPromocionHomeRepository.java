package com.mercaextra.app.repository;

import com.mercaextra.app.domain.ProductoPromocionHome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductoPromocionHome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoPromocionHomeRepository extends JpaRepository<ProductoPromocionHome, Long> {}
