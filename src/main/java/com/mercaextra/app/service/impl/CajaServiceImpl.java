package com.mercaextra.app.service.impl;

import static org.springframework.data.jpa.domain.Specification.where;

import com.mercaextra.app.config.Utilities;
import com.mercaextra.app.domain.Caja;
import com.mercaextra.app.repository.CajaRepository;
import com.mercaextra.app.service.CajaService;
import com.mercaextra.app.service.dto.CajaDTO;
import com.mercaextra.app.service.dto.ReporteCajaDTO;
import com.mercaextra.app.service.mapper.CajaMapper;
import com.mercaextra.app.specification.CajaSpecification;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caja}.
 */
@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    private final CajaRepository cajaRepository;

    private final CajaMapper cajaMapper;

    public CajaServiceImpl(CajaRepository cajaRepository, CajaMapper cajaMapper) {
        this.cajaRepository = cajaRepository;
        this.cajaMapper = cajaMapper;
    }

    @Override
    public CajaDTO save(CajaDTO cajaDTO) {
        log.debug("Request to save Caja : {}", cajaDTO);
        Caja caja = cajaMapper.toEntity(cajaDTO);
        caja = cajaRepository.save(caja);
        return cajaMapper.toDto(caja);
    }

    @Override
    public Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO) {
        log.debug("Request to partially update Caja : {}", cajaDTO);

        return cajaRepository
            .findById(cajaDTO.getId())
            .map(existingCaja -> {
                cajaMapper.partialUpdate(existingCaja, cajaDTO);

                return existingCaja;
            })
            .map(cajaRepository::save)
            .map(cajaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> findAll() {
        log.debug("Request to get all Cajas");
        return cajaRepository.findAll().stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> findOne(Long id) {
        log.debug("Request to get Caja : {}", id);
        return cajaRepository.findById(id).map(cajaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caja : {}", id);
        cajaRepository.deleteById(id);
    }

    @Override
    public List<CajaDTO> cajaByFilters(CajaDTO caja) {
        log.debug("Request to get cajas by filters.");

        List<Caja> cajas = cajaRepository.findAll(
            where(CajaSpecification.cajaByValue(caja.getValorTotalDia()).and(CajaSpecification.cajaByEstado(caja.getEstado())))
        );

        return cajas.stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    // Tarea programada para recordar la creacino de una caja diaria en caso de que se haya registrado una o mas compras durante el dia
    // @Scheduled(cron = "0 */1 * ? * *")
    public Boolean RememberCreationCaja() {
        log.debug("Request to  remember create  caja ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String fecha = Utilities.validateDate(Instant.now());
        Date date;
        boolean resp = false;
        try {
            date = format.parse(fecha);
            String fechaFormat = format.format(date);
            resp = Boolean.parseBoolean(cajaRepository.booleanResult(fechaFormat));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resp;
        // VALIDAMOS QUE EXISTAN FACTURAS CREADAS EN EL DIA.

    }

    // Metodo para traer el valor total de todo lo que se ha vendido en el dia (Facturas).
    @Override
    public BigDecimal valorVendidoDia() {
        log.debug("Request to get total daily value");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        // Se formatea la fecha para la consulta
        String fechaFormat = format.format(date);

        BigDecimal valorVendidoDia = cajaRepository.valorVendidoDia(fechaFormat);

        return valorVendidoDia;
    }

    @Override
    public byte[] exportarPdf(String fechaInicio, String fechaFin) throws IOException {
        log.debug("Request to generate report of cajas");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(fechaInicio);
            Date date2 = format.parse(fechaFin);

            if (date.after(date2)) {
                throw new BadRequestAlertException(fechaFin, "La fecha inicio no puede ser mayor a la fecha fin.", fechaFin);
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        String fechaInicioFormat = fechaInicio.substring(0, 10);
        String fechaFinFormat = fechaFin.substring(0, 10);
        File pdfFile = File.createTempFile("invoice", ".pdf");

        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {
            List<Caja> cajas = cajaRepository.cajasFechas(fechaInicioFormat, fechaFinFormat);

            List<ReporteCajaDTO> cajaData = cajas
                .stream()
                .map(element -> {
                    ReporteCajaDTO reporte = new ReporteCajaDTO();
                    reporte.setFechaCreacion(element.getFechaCreacion().toString().substring(0, 10));
                    reporte.setValorDia(element.getValorTotalDia());
                    reporte.setValorRegistrado(element.getValorRegistradoDia());
                    reporte.setDiferencia(element.getDiferencia());
                    reporte.setEstado(element.getEstado());

                    return reporte;
                })
                .collect(Collectors.toCollection(LinkedList::new));

            //final File file = ResourceUtils.getFile("cajaReporte.jrxml");
            String fileS = "reporteCaja.jrxml";
            //final JasperReport report = (JasperReport) JRLoader.loadObject(file);
            JasperDesign jasperDesing = JRXmlLoader.load(fileS);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesing);

            double valorTotal = cajas.stream().mapToDouble(value -> value.getValorTotalDia().doubleValue()).sum();
            String val = Double.toString(valorTotal);

            //String parameter = "dsada";
            JRBeanCollectionDataSource collection = new JRBeanCollectionDataSource(cajaData);
            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", collection);
            parameters.put("total", val);
            parameters.put("fechaInicio", fechaInicioFormat);
            parameters.put("fechaFin", fechaFinFormat);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "reportCajaFecha");
            byte[] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
            /*
             * String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
             * StringBuilder stringBuilder = new StringBuilder().append("InvoicePDF:");
             * ContentDisposition contentDisposition =
             * ContentDisposition.builder("attachment")
             * .filename(stringBuilder.append("reportCajaFecha") .append("generateDate:")
             * .append(sdf) .append(".pdf") .toString()) .build(); HttpHeaders headers = new
             * HttpHeaders();
             *
             * //byte[] bytes = Files.readAllBytes(Paths.get(pdfFile.getAbsolutePath()));
             *
             * headers.setContentDisposition(contentDisposition);
             *
             *
             */

            return reporte;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return null;
        }
    }
}
