package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Comentario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    @Query("SELECT c FROM Comentario c WHERE c.idProducto = :idProducto ORDER BY c.fechaComentario DESC")
    List<Comentario> ComentsProducts(@Param("idProducto") Long idProducto);

    @Query("SELECT c FROM Comentario c WHERE c.idProducto =:idProducto AND c.id = :idComentario")
    List<Comentario> respuestaComentarios(@Param("idProducto") Long idProducto, @Param("idComentario") Long idComentario);
}
