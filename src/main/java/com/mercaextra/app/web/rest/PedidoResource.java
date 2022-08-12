package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.PedidoRepository;
import com.mercaextra.app.service.PedidoService;
import com.mercaextra.app.service.dto.FacturaPedidoDTO;
import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.Pedido}.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

    private static final String ENTITY_NAME = "pedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PedidoService pedidoService;

    private final PedidoRepository pedidoRepository;

    public PedidoResource(PedidoService pedidoService, PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * {@code POST  /pedidos} : Create a new pedido.
     *
     * @param pedidoDTO the pedidoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedidoDTO, or with status {@code 400 (Bad Request)} if the pedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody PedidoDTO pedidoDTO) throws URISyntaxException {
        log.debug("REST request to save Pedido : {}", pedidoDTO);
        if (pedidoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PedidoDTO result = pedidoService.save(pedidoDTO);
        return ResponseEntity
            .created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pedidos/:id} : Updates an existing pedido.
     *
     * @param id the id of the pedidoDTO to save.
     * @param pedidoDTO the pedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PedidoDTO pedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pedido : {}, {}", id, pedidoDTO);
        if (pedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PedidoDTO result = pedidoService.save(pedidoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/pedidosPengindInvoices")
    public List<FacturaPedidoDTO> pengingInvoices() {
        log.debug("REST request to get all pending invoices");
        return pedidoService.checkPendingInvoice();
    }

    /**
     * {@code PATCH  /pedidos/:id} : Partial updates given fields of an existing pedido, field will ignore if it is null
     *
     * @param id the id of the pedidoDTO to save.
     * @param pedidoDTO the pedidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pedidoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pedidos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PedidoDTO> partialUpdatePedido(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PedidoDTO pedidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pedido partially : {}, {}", id, pedidoDTO);
        if (pedidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PedidoDTO> result = pedidoService.partialUpdate(pedidoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDTO.getId().toString())
        );
    }

    @GetMapping("/pedidoConfirmarTransaccion/{idFactura}")
    public ResponseEntity<Void> confirmarTransaccion(@PathVariable Long idFactura) {
        log.debug("REST request to confirm transaction");

        if (null == idFactura) {
            throw new BadRequestAlertException(applicationName, ENTITY_NAME, "idNull");
        }

        pedidoService.confirmarTranssacion(idFactura);
        return ResponseEntity.ok().body(null);
    }

    /**
     * {@code GET  /pedidos} : get all the pedidos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pedidos in body.
     */
    @GetMapping("/pedidos")
    public List<PedidoDTO> getAllPedidos() {
        log.debug("REST request to get all Pedidos");
        return pedidoService.findAll();
    }

    @GetMapping("/pedido-comming")
    public ResponseEntity<PedidoDTO> pedidoComming() throws URISyntaxException {
        log.debug("REST request to get pedido in comming");

        PedidoDTO pedido = pedidoService.pedidoEntrega();
        /*
         * if (pedido == null) { throw new
         * BadRequestAlertException("NO hay pedidos en entrega", ENTITY_NAME,
         * "no existe"); }
         */
        return new ResponseEntity<PedidoDTO>(pedido, HttpStatus.OK);
    }

    /**
     * {@code GET  /pedidos/:id} : get the "id" pedido.
     *
     * @param id the id of the pedidoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedidoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> getPedido(@PathVariable Long id) {
        log.debug("REST request to get Pedido : {}", id);
        Optional<PedidoDTO> pedidoDTO = pedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedidoDTO);
    }

    /**
     * {@code DELETE  /pedidos/:id} : delete the "id" pedido.
     *
     * @param id the id of the pedidoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @GetMapping("/pedidos-facturas")
    public List<FacturaPedidoDTO> facturasPedido() {
        log.debug("REST request to get all facturas pedidos per userName");
        return pedidoService.facturasLogin();
    }

    @PostMapping("/pedido-finalizado")
    public ResponseEntity<Void> pedidoFinalizado(@RequestBody PedidoDTO pedidoDTO) throws URISyntaxException {
        log.debug("REST request to change state pedido to finalised");

        if (pedidoDTO.getId() == null) {
            throw new BadRequestAlertException("ID is null", ENTITY_NAME, ENTITY_NAME);
        }

        if (!pedidoRepository.existsById(pedidoDTO.getId())) {
            throw new BadRequestAlertException("ID not exist", ENTITY_NAME, ENTITY_NAME);
        }

        pedidoService.pedidoFinalizado(pedidoDTO);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("pedidos-fecha/{fecha}")
    public List<PedidoDTO> pedidosPorFecha(@PathVariable String fecha) {
        log.debug("REST request to get all pedido per date", fecha);
        return pedidoService.pedidosFecha(fecha);
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        log.debug("REST request to delete Pedido : {}", id);
        pedidoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/pedido-validate-domiciliary")
    public int validateAvibleDomiciliary() {
        log.debug("REST request to validate aviable domiciliary");
        return pedidoService.validarDomiciliario();
    }
}
