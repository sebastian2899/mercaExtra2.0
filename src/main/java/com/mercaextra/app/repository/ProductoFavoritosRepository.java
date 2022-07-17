package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.domain.ProductoFavoritos;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductoFavoritos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoFavoritosRepository extends JpaRepository<ProductoFavoritos, Long>, JpaSpecificationExecutor<ProductoFavoritos> {
    @Query("SELECT MAX(pf.lastUpdate) FROM ProductoFavoritos pf")
    Instant lastUpdate();

    @Query(
        "SELECT p.id,p.nombre,p.precio,p.imagen,p.imagenContentType,p.categoria FROM Producto p INNER JOIN ProductoFavoritos pf ON pf.idProduct = p.id AND pf.login =:login AND pf.estado = :estado ORDER BY pf.puesto"
    )
    List<Object[]> favoriteProduts(@Param("login") String login, @Param("estado") String estado);

    @Query("SELECT pf FROM ProductoFavoritos pf WHERE pf.login = :login ORDER BY puesto")
    List<ProductoFavoritos> favPorLogin(@Param("login") String login);

    @Query("SELECT pf FROM ProductoFavoritos pf WHERE pf.idProduct = :idProduct")
    ProductoFavoritos productoFavPorIdProduc(@Param("idProduct") Long idProduct);
}
