package com.mercaextra.app.repository;

import com.mercaextra.app.domain.ProductoPromocionHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductoPromocionHome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoPromocionHomeRepository extends JpaRepository<ProductoPromocionHome, Long> {
    @Query(
        value = "SELECT CASE WHEN EXISTS (SELECT id FROM producto_promocion_home WHERE id_producto =:idProducto)" +
        "THEN 'true' ELSE 'false' END",
        nativeQuery = true
    )
    String resp(@Param("idProducto") Long idProducto);
}
