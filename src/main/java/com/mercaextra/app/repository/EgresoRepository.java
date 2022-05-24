package com.mercaextra.app.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mercaextra.app.domain.Egreso;

/**
 * Spring Data SQL repository for the Egreso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {
	
	@Query("SELECT SUM(e.valorEgreso) FROM Egreso e WHERE DATE_FORMAT(e.fechaCreacion, '%Y-%m-%d') = :fecha")
	BigDecimal valueDailyEgreso(@Param("fecha")String fecha);
	
	
	@Query("SELECT e FROM Egreso e WHERE DATE_FORMAT(e.fechaCreacion, '%Y-%m-%d') = :fecha")
	List<Egreso>dailyEgress(@Param("fecha") String fecha);
	
}
