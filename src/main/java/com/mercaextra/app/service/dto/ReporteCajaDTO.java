package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReporteCajaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String fechaCreacion;
    private BigDecimal valorDia;
    private BigDecimal valorRegistrado;
    private BigDecimal diferencia;
    private String estado;

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorDia() {
        return valorDia;
    }

    public void setValorDia(BigDecimal valorDia) {
        this.valorDia = valorDia;
    }

    public BigDecimal getValorRegistrado() {
        return valorRegistrado;
    }

    public void setValorRegistrado(BigDecimal valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
