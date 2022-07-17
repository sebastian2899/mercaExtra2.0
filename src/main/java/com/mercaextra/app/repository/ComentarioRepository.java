package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Comentario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Comentario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {}
