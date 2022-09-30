package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.domain.ItemFacturaVenta;
import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.domain.enumeration.MetodoPago;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.repository.ItemFacturaVentaRepository;
import com.mercaextra.app.service.FacturaService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.mapper.FacturaMapper;
import com.mercaextra.app.service.mapper.ProductoMapper;
import com.mercaextra.app.service.utils.GenerateExcel;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import liquibase.repackaged.org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    private final ItemFacturaVentaRepository itemFacturaVentaRepository;

    private final ProductoMapper productosMapper;

    private final UserService userService;

    private final GenerateExcel generateExcel;

    @PersistenceContext
    private EntityManager entityManager;

    public FacturaServiceImpl(
        FacturaRepository facturaRepository,
        FacturaMapper facturaMapper,
        ItemFacturaVentaRepository itemFacturaVentaRepository,
        ProductoMapper productosMapper,
        UserService userService,
        GenerateExcel generateExcel
    ) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
        this.productosMapper = productosMapper;
        this.userService = userService;
        this.generateExcel = generateExcel;
    }

    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);

        // SE RECUPERA EL USUARIO LOGUEADO PARA GUARDAR LA FACTURA A ESE LOGIN.
        String loginName = userService.getUserWithAuthorities().get().getLogin().toString();
        factura.setUserName(loginName);

        //LA FECHA DE CREACION DE LA FACTURA ES LA FECHA INMEDIATA EN LA QUE SE CREA LA FACTURA.
        factura.setFechaCreacion(Instant.now());

        //SE GENERA UN NUMERO ALEATORIO DE 1000 A 10000 Y SE LE SETEA A EL NUMERO DE FACTURA
        //EN CASO DE QUE ALGUN NUMERO DE FACTURA SE REPITA AUN SE PUEDEN DISTINGUIR CON EL USERNAME O CON EL ID

        factura.numeroFactura(RamdomNumber());

        // DEPENDIENTE DE EL METODO DE PAGO SE SETEA EL TIPO DE FACTURA.
        if (factura.getMetodoPago().equals(MetodoPago.TARJETA_CREDITO)) {
            factura.setTipoFactura(TipoFactura.CREDITO);
        } else if (factura.getMetodoPago().equals(MetodoPago.CONTRA_ENTREGA)) {
            factura.setTipoFactura(TipoFactura.CONTADO);
            factura.setEstadoFactura("Lista");
        } else if (factura.getMetodoPago().equals(MetodoPago.TRANSACCION_BANCARIA)) {
            factura.setTipoFactura(TipoFactura.TRANSACCION);
            factura.setEstadoFactura("Transaccion Pendiente");
        }

        factura = facturaRepository.save(factura);

        ItemFacturaVenta itemFacturaVenta = null;

        if (factura.getItemsPorFactura() != null) {
            for (ItemFacturaVenta item : factura.getItemsPorFactura()) {
                itemFacturaVenta = new ItemFacturaVenta();
                itemFacturaVenta.setIdFactura(factura.getId());
                itemFacturaVenta.setIdProducto(item.getIdProducto());
                itemFacturaVenta.setCantidad(item.getCantidad());
                itemFacturaVenta.setPrecio(item.getPrecio());

                //GUARDO EL ITEM
                itemFacturaVentaRepository.save(itemFacturaVenta);

                //SE RESTAN LA CANTIDAD DE PRODUCTOS VENDIDOS (INDIVIDUAL)
                Query q = entityManager
                    .createQuery(Constants.RESTAR_PRODUCTOS_VENDIDOS)
                    .setParameter("cantidad", itemFacturaVenta.getCantidad())
                    .setParameter("id", itemFacturaVenta.getIdProducto());

                q.executeUpdate();
            }
        }

        return facturaMapper.toDto(factura);
    }

    @Override
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        log.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDTO> findAll() {
        log.debug("Request to get all Facturas");

        //SEGUN EL USUARIO LOGEADO SE RETORNAN SUS DATOS CORRESPONDIENTES.
        String login = userService.getUserWithAuthorities().get().getLogin();

        if (login == "admin") {
            return facturaRepository.findAll().stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        } else {
            Query q = entityManager.createQuery("SELECT f FROM Factura f WHERE f.userName =:userName").setParameter("userName", login);

            List<Factura> inoviceLogin = q.getResultList();

            return inoviceLogin.stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        }
    }

    private String RamdomNumber() {
        Random numeroFactura = new Random();
        int numRamdon = numeroFactura.nextInt(10000 - 1000 + 1) + 10000;
        String numRamdonString = String.valueOf(numRamdon);
        return numRamdonString;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDTO> facturasPorUsuario() {
        log.debug("Request to get all facturas per user");
        String userName = userService.getUserWithAuthorities().get().getLogin();
        Query q = entityManager.createQuery(Constants.TRAER_FACTURAS_POR_USUARIO).setParameter("userName", userName);

        List<Factura> facturasPorUsuario = q.getResultList();
        return facturasPorUsuario.stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public FacturaDTO repurcharseInvoice(FacturaDTO facturaDto) {
        log.debug("Request to repurcharse factura", facturaDto);

        Factura factura = facturaMapper.toEntity(facturaDto);
        Factura newFactura = factura;

        //NOS ASEGURAMOS DIRECTAMENTE DESDE EL METODO DE LOGIN EL USUARIO QUE ACABA DE HACER LA PETICION PARA RECOMPRAR LA FACTURA
        String userName = userService.getUserWithAuthorities().get().getLogin();

        //SE CAMBIAN LOS VALORES PARA LA NUEVA FACURA
        newFactura.setId(null);
        newFactura.setFechaCreacion(Instant.now());
        newFactura.setUserName(userName);
        newFactura.setNumeroFactura(RamdomNumber());

        if (factura.getMetodoPago().equals(MetodoPago.CONTRA_ENTREGA)) {
            newFactura.setValorPagado(factura.getValorPagado());
            newFactura.setValorDeuda(factura.getValorDeuda());
            newFactura.setEstadoFactura("lista");
        } else if (factura.getMetodoPago().equals(MetodoPago.TRANSACCION_BANCARIA)) {
            newFactura.setEstadoFactura("Transaccion Pendiente");
        }

        facturaRepository.save(newFactura);

        List<ItemFacturaVenta> itemsFactura = consultarItemsPorFactura(facturaDto.getId());

        ItemFacturaVenta itemFactura = null;
        if (itemsFactura != null) {
            for (ItemFacturaVenta item : itemsFactura) {
                itemFactura = new ItemFacturaVenta();
                itemFactura.setIdFactura(newFactura.getId());
                itemFactura.setCantidad(item.getCantidad());
                itemFactura.setIdProducto(item.getIdProducto());
                itemFactura.setPrecio(item.getPrecio());

                itemFacturaVentaRepository.save(itemFactura);
                Query q = entityManager
                    .createQuery(Constants.RESTAR_PRODUCTOS_VENDIDOS)
                    .setParameter("cantidad", item.getCantidad())
                    .setParameter("id", item.getIdProducto());

                q.executeUpdate();
            }
        }

        return facturaMapper.toDto(newFactura);
    }

    @Transactional
    @Override
    public List<ProductoDTO> productosDisponibles() {
        return facturaRepository
            .productosDisponibles()
            .stream()
            .map(productosMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaDTO findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        String userName = userService.getUserWithAuthorities().get().getLogin().toString();
        if (userName != "admin") {
            Factura facturaAdmin = facturaRepository.facturaId(id);
            facturaAdmin.setItemsPorFactura(consultarItemsPorFactura(id));
            return facturaMapper.toDto(facturaAdmin);
        } else {
            Query q = entityManager
                .createQuery(Constants.TRAER_FACTURA_POR_USUARIO)
                .setParameter("userName", userName)
                .setParameter("id", id);
            Factura factura = (Factura) q.getSingleResult();
            factura.setItemsPorFactura(consultarItemsPorFactura(id));
            return facturaMapper.toDto(factura);
        }
    }

    /*
     * SE CONSULTAR TODOS LOS ITEMS REFERENTES A LA FACTURA QUE QUEREMOS CONSULTAR Y
     * DE ACUERDO AL ID DE LA FACTURA SE RECUPERAN LOS NOMBRES CORRESPONDIENTES, YA
     * QUE EL NOMBRE DEL PRODUCTO EN LA ENTIDAD ITEMFACTURA ES UN TRANSITORIO
     */
    private List<ItemFacturaVenta> consultarItemsPorFactura(Long id) {
        log.debug("Request to get all items per factura.", id);

        List<ItemFacturaVenta> itemsFactura = itemFacturaVentaRepository.itemsPorFactura(id);
        for (ItemFacturaVenta item : itemsFactura) {
            String nombreProducto = facturaRepository.nombreProdcuto(item.getIdProducto());
            item.setNombreProducto(nombreProducto);
        }

        return itemsFactura;
    }

    @Override
    public BigDecimal valuePerDates(String fechaInicio, String fechaFin) {
        log.debug("Request to get value of invoice per dates");

        String fechaInicioFormat = fechaInicio.substring(0, 10);
        String fechaFinFormat = fechaFin.substring(0, 10);

        BigDecimal value = facturaRepository.valuePerDates(fechaInicioFormat, fechaFinFormat);
        BigDecimal resp;
        resp = value == null ? resp = BigDecimal.ZERO : value;

        return resp;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> productosCategoria(String categoria) {
        log.debug("Request to get productos per categoria", categoria);

        List<Producto> productosCategoria = facturaRepository.productoPorCategoria(categoria);

        return productosCategoria.stream().map(productosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDTO> invoiicesByFilters(FacturaDTO facturaDto) {
        log.debug("Request to get invoices per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashedMap<>();

        //Base
        sb.append(Constants.FACTURA_BASE);

        if (facturaDto.getFechaCreacion() != null) {
            sb.append(Constants.FACTURA_FECHA);
            String fechaFormat = facturaDto.getFechaCreacion().toString().substring(0, 10);
            filtros.put("fecha", fechaFormat);
        }

        if (facturaDto.getMetodoPago() != null) {
            sb.append(Constants.FACTURA_METODOPAGO);
            filtros.put("metodoPago", facturaDto.getMetodoPago());
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Factura> productsFiltro = q.getResultList();
        return productsFiltro.stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public byte[] zipFileInvoice() throws IOException {
        log.info("Request to create zip");

        // FECHA ACTUAL
        String fechaActual = Instant.now().toString().substring(0, 10);

        Calendar calendarMesAnterior = Calendar.getInstance();
        calendarMesAnterior.add(Calendar.MONTH, -1);

        Instant calendarToInstant = calendarMesAnterior.toInstant();
        String fechaMesAnterior = calendarToInstant.toString().substring(0, 10);

        List<Factura> facturas = facturaRepository.rangeMonthInvoice(fechaMesAnterior, fechaActual);

        List<Object> invocieString;

        invocieString =
            facturas
                .stream()
                .map(element -> {
                    LinkedHashMap<String, String> data = new LinkedHashMap<>();
                    data.put("fecha", element.getFechaCreacion().toString().substring(0, 10));
                    data.put("cliente", element.getInfoCiente());
                    data.put("numFac", element.getNumeroFactura());
                    data.put("tipoFa", element.getTipoFactura().toString());
                    data.put("vafac", element.getValorFactura().toString());
                    data.put("valpa", element.getValorPagado().toString());
                    data.put("valde", element.getValorDeuda().toString());
                    data.put("facest", element.getEstadoFactura());

                    return data;
                })
                .collect(Collectors.toList());

        return generateExcel.writeExcel(invocieString, Constants.invoiceColumns);
    }
}
