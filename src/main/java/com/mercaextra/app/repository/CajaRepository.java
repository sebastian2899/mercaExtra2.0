package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Caja;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Caja entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    @Query("SELECT SUM(f.valorFactura) FROM Factura f WHERE DATE_FORMAT(f.fechaCreacion, '%d/%m/%Y')=:fecha")
    BigDecimal valorVendidoDia(@Param("fecha") String fecha);

    @Query(
        value = "SELECT CASE WHEN EXISTS (SELECT f.estado_factura FROM factura AS f WHERE DATE_FORMAT(f.fecha_creacion,'%d/%m/%Y') = :fecha)" +
        " THEN 'true' ELSE 'false' END",
        nativeQuery = true
    )
    String booleanResult(@Param("fecha") String fecha);

    @Query("SELECT c FROM Caja c WHERE DATE_FORMAT(c.fechaCreacion, '%Y-%m-%d') BETWEEN :fechaInicio AND :fechaFin")
    List<Caja> cajasFechas(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);
}
