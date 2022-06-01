package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.service.dto.DatosReembolsoAConcluirDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reembolso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {
    @Query(
        "SELECT p.id,p.fechaPedido,p.direccion,f.valorFactura,f.id,d.nombre,d.id,p.fechaExpiReembolso FROM Pedido p INNER JOIN Factura f ON p.idFactura = f.id" +
        " INNER JOIN Domiciliario d ON p.idDomiciliario = d.id WHERE p.userName =:userName AND p.estado = 'Expirado'"
    )
    List<Object[]> pedidosReembolso(@Param("userName") String userName);

    @Query(
        "SELECT r.id,p.fechaPedido,d.nombre,r.fechaReembolso,r.estado,r.descripcion,d.id,r.metodoReembolso FROM Reembolso r INNER JOIN Pedido p ON r.idPedido = p.id" +
        " INNER JOIN Domiciliario d ON r.idDomiciliario = d.id WHERE r.estado = :estado"
    )
    List<Object[]> dataOrders(@Param("estado") String estado);

    @Query("SELECT r FROM Reembolso r WHERE r.id =:id")
    Reembolso refundById(@Param("id") Long id);

    @Query(
        "SELECT f.valorFactura,p.fechaPedido,r.descripcion,p.userName,r.id FROM Reembolso r INNER JOIN Pedido p ON r.idPedido = p.id" +
        " INNER JOIN Factura f ON p.idFactura = f.id WHERE r.id =:id"
    )
    List<Object[]> dataRefundInProcess(@Param("id") Long id);
}
