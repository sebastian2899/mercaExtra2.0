package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.repository.PedidoRepository;
import com.mercaextra.app.repository.ReembolsoRepository;
import com.mercaextra.app.service.ReembolsoService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.DatosPedidoReembolsoDTO;
import com.mercaextra.app.service.dto.DatosReembolsoAConcluirDTO;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import com.mercaextra.app.service.mapper.ReembolsoMapper;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reembolso}.
 */
@Service
@Transactional
public class ReembolsoServiceImpl implements ReembolsoService {

    private final Logger log = LoggerFactory.getLogger(ReembolsoServiceImpl.class);

    private final ReembolsoRepository reembolsoRepository;

    private final ReembolsoMapper reembolsoMapper;

    private static final String ENTITY_NAME = "reembolso";

    private final PedidoRepository pedidoRepository;

    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    public ReembolsoServiceImpl(
        ReembolsoRepository reembolsoRepository,
        ReembolsoMapper reembolsoMapper,
        UserService userService,
        PedidoRepository pedidoRepository
    ) {
        this.reembolsoRepository = reembolsoRepository;
        this.reembolsoMapper = reembolsoMapper;
        this.userService = userService;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public ReembolsoDTO save(ReembolsoDTO reembolsoDTO) {
        log.debug("Request to save Reembolso : {}", reembolsoDTO);
        Reembolso reembolso = reembolsoMapper.toEntity(reembolsoDTO);
        Pedido pedido = null;

        if (reembolsoDTO.getId() != null) {
            reembolso = reembolsoRepository.refundById(reembolso.getId());
        }

        // Consultamos el pedido para cambiarlo de estado.
        if (reembolso.getIdPedido() != null) {
            pedido = pedidoRepository.getById(reembolso.getIdPedido());
        }

        // SE VUELVE A VALIDAR QUE LA FECHA LIMITE NO ESTE ANTES DE LA FECHA ACTUAL.
        if (pedido.getFechaExpiReembolso().isBefore(Instant.now())) {
            throw new BadRequestAlertException("Fecha expirada", ENTITY_NAME, "Fecha limite vencida");
        }

        if (reembolsoDTO.getId() == null) {
            reembolso.setEstado("Reembolso en estudio");
            pedido.setEstado("Reembolso en estudio");
        } else {
            pedido.setEstado("Reembolsado");
            String estado = reembolso.getEstado().equals("Concluido") ? "Reembolsado" : "Concluido";
            reembolso.setEstado(estado);
        }

        pedidoRepository.save(pedido);
        reembolso = reembolsoRepository.save(reembolso);
        return reembolsoMapper.toDto(reembolso);
    }

    public List<ReembolsoDTO> reembolsosConcluidos() {
        log.debug("Request to get all Reembolsos");
        List<Object[]> reembolsos = reembolsoRepository.dataOrders("Concluido");

        List<ReembolsoDTO> reembolsosDTO = reembolsos
            .stream()
            .map(element -> {
                ReembolsoDTO reembolsoDTO = new ReembolsoDTO();
                reembolsoDTO.setId(Long.parseLong(element[0].toString()));
                reembolsoDTO.setFechaPedido(element[1].toString().substring(0, 10));
                reembolsoDTO.setNombreDomiciliario(element[2].toString());
                reembolsoDTO.setFechaReembolso(Instant.parse(element[3].toString()));
                reembolsoDTO.setEstado(element[4].toString());
                reembolsoDTO.setDescripcion(element[5].toString());
                return reembolsoDTO;
            })
            .collect(Collectors.toList());

        return reembolsosDTO;
    }

    public List<ReembolsoDTO> refoundInStudy() {
        log.debug("Request to get refund in study state");

        // SE RECUPERAN LOS DATOS
        List<Object[]> dataSet = reembolsoRepository.dataOrders("Reembolso en estudio");

        List<ReembolsoDTO> dataSetDTO = new ArrayList<>();

        dataSet
            .stream()
            .map(element -> {
                ReembolsoDTO reembolsoDTO = new ReembolsoDTO();
                reembolsoDTO.setId(Long.parseLong(element[0].toString()));
                reembolsoDTO.setFechaPedido(element[1].toString().substring(0, element[1].toString().indexOf("T")));
                reembolsoDTO.setNombreDomiciliario(element[2].toString());
                reembolsoDTO.setFechaReembolso(Instant.parse(element[3].toString()));
                reembolsoDTO.setEstado(element[4].toString());
                reembolsoDTO.setDescripcion(element[5].toString());

                return reembolsoDTO;
            })
            .forEach(dataSetDTO::add);

        return dataSetDTO;
    }

    @Override
    public List<ReembolsoDTO> refundByOption(Long option) {
        log.debug("Request to get refund by option");

        // SE RECUPERAN LOS DATOS SEGUN LA OPCION

        switch (option.intValue()) {
            case 1:
                return findAll();
            case 2:
                return refoundInStudy();
            case 3:
                return reembolsosConcluidos();
            default:
                throw new BadRequestAlertException("Opcion invalida", ENTITY_NAME, "Opcion invalida");
        }
    }

    @Override
    public Optional<ReembolsoDTO> partialUpdate(ReembolsoDTO reembolsoDTO) {
        log.debug("Request to partially update Reembolso : {}", reembolsoDTO);

        return reembolsoRepository
            .findById(reembolsoDTO.getId())
            .map(existingReembolso -> {
                reembolsoMapper.partialUpdate(existingReembolso, reembolsoDTO);

                return existingReembolso;
            })
            .map(reembolsoRepository::save)
            .map(reembolsoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReembolsoDTO> findAll() {
        log.debug("Request to get all Reembolsos");
        return reembolsoRepository.findAll().stream().map(reembolsoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReembolsoDTO> findOne(Long id) {
        log.debug("Request to get Reembolso : {}", id);

        return Optional
            .of(reembolsoRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(reembolso -> {
                ReembolsoDTO reembolsoDTO = new ReembolsoDTO();
                reembolsoDTO.setId(reembolso.getId());
                reembolsoDTO.setDescripcion(reembolso.getDescripcion());
                reembolsoDTO.setFechaReembolso(reembolso.getFechaReembolso());
                reembolsoDTO.setIdPedido(reembolso.getIdPedido());
                reembolsoDTO.setFechaPedido(fechaPedido(reembolso.getIdPedido()));
                reembolsoDTO.setEstado(reembolso.getEstado());
                reembolsoDTO.setNombreDomiciliario(nomDomiciliario(reembolso.getIdDomiciliario()));

                return reembolsoDTO;
            });
    }

    private String fechaPedido(Long id) {
        Query q = entityManager.createQuery("SELECT p.fechaPedido FROM Pedido p " + "WHERE p.id=:id").setParameter("id", id);

        String fecha = q.getSingleResult().toString().substring(0, q.getSingleResult().toString().indexOf("T")).toString();

        return fecha;
    }

    private String nomDomiciliario(Long id) {
        Query q = entityManager.createQuery("SELECT d.nombre FROM Domiciliario d " + "WHERE d.id=:id").setParameter("id", id);

        String name = q.getSingleResult().toString();

        return name;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reembolso : {}", id);
        reembolsoRepository.deleteById(id);
    }

    @Override
    public List<DatosPedidoReembolsoDTO> pedidosExpirados() {
        log.debug("Request to get all expired orders");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String userName = userService.getUserWithAuthorities().get().getLogin();

        List<Object[]> expirados = reembolsoRepository.pedidosReembolso(userName);

        List<DatosPedidoReembolsoDTO> pedidosExpirados = new ArrayList<>();

        expirados
            .stream()
            .map(element -> {
                DatosPedidoReembolsoDTO pedido = new DatosPedidoReembolsoDTO();
                pedido.setIdPedido(Long.parseLong(element[0].toString()));
                pedido.setFechaPedido(element[1].toString().substring(0, element[1].toString().indexOf(("T"))).toString());
                pedido.setPedidoDireccion(element[2].toString());
                pedido.setValorFactura(new BigDecimal(element[3].toString()));
                pedido.setIdFactura(Long.parseLong(element[4].toString()));
                pedido.setDomiciliario(element[5].toString());
                pedido.setIdDomiciliario(Long.parseLong(element[6].toString()));
                pedido.setFechaExpiPedido(element[7].toString().substring(0, element[7].toString().indexOf("T")).toString());

                return pedido;
            })
            .forEach(pedidosExpirados::add);

        return pedidosExpirados.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public DatosReembolsoAConcluirDTO dataRefundInProcess(Long id) {
        log.debug("Request to get data refund in process");

        List<Object[]> datos = reembolsoRepository.dataRefundInProcess(id);

        DatosReembolsoAConcluirDTO reembolsoTemp = datos
            .stream()
            .map(element -> {
                DatosReembolsoAConcluirDTO reembolso = new DatosReembolsoAConcluirDTO();
                reembolso.setValorFactura(new BigDecimal(element[0].toString()));
                reembolso.setFechaPedido(element[1].toString().substring(0, element[1].toString().indexOf("T")).toString());
                reembolso.setDescripcion(element[2].toString());
                reembolso.setNombreUsuario(element[3].toString());
                reembolso.setId(Long.parseLong(element[4].toString()));

                return reembolso;
            })
            .collect(Collectors.toList())
            .get(0);

        return reembolsoTemp;
    }
}
