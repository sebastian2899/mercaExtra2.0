package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class DatosReembolsoAConcluirDTO implements Serializable {

    private Long id;

    private String fechaPedido;

    private BigDecimal valorFactura;

    private String descripcion;

    private String nombreUsuario;

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosReembolsoAConcluirDTO that = (DatosReembolsoAConcluirDTO) o;
        return (
            Objects.equals(fechaPedido, that.fechaPedido) &&
            Objects.equals(valorFactura, that.valorFactura) &&
            Objects.equals(descripcion, that.descripcion)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaPedido, valorFactura, descripcion);
    }
}
