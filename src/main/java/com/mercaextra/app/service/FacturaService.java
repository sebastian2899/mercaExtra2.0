package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.dto.ProductoDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Factura}.
 */
public interface FacturaService {
    /**
     * Save a factura.
     *
     * @param facturaDTO the entity to save.
     * @return the persisted entity.
     */
    FacturaDTO save(FacturaDTO facturaDTO);

    /**
     * Partially updates a factura.
     *
     * @param facturaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO);

    /**
     * Get all the facturas.
     *
     * @return the list of entities.
     */
    List<FacturaDTO> findAll();

    /**
     * Get the "id" factura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    FacturaDTO findOne(Long id);

    /**
     * Delete the "id" factura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductoDTO> productosDisponibles();

    List<ProductoDTO> productosCategoria(String categoria);

    List<FacturaDTO> facturasPorUsuario();

    /*
     * con este metodo podemos recomprar una factura, recibimos los datos de la
     * factura que ya existe y creamos una factura nueva con los datos
     * correspondientes, la fecha de creacion cambia, el id cambia junto con los
     * valores que se suponen deben ser nuevos de acuerdo a la facura
     */
    FacturaDTO repurcharseInvoice(FacturaDTO facturaDto);

    BigDecimal valuePerDates(String fechaInicio, String fechaFin);

    List<FacturaDTO> invoiicesByFilters(FacturaDTO facturaDto);

    byte[] zipFileInvoice() throws IOException;
}
