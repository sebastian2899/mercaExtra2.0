package com.mercaextra.app.service.utils;

import com.mercaextra.app.service.impl.CajaServiceImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenerateExcel {

    private static final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    @SuppressWarnings("unchecked")
    public byte[] writeExcel(List<Object> data, List<String> headers) throws IOException {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Facturas");
        int rowCount = 1;

        createHeader(sheet, headers);
        Map<String, byte[]> mapFileConvert = new HashMap<>();

        log.debug("Incia generacion de excel");
        for (int i = 0; i < data.size(); i++) {
            createFile((LinkedHashMap<String, String>) data.get(i), sheet.createRow(rowCount), headers.size());
            rowCount++;
        }

        mapFileConvert.put("FacturaReport" + ".xlsx", saveFile(workbook, rowCount));
        return ZipFile.fileComprime(mapFileConvert);
    }

    public void createHeader(Sheet sheet, List<String> headers) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            row.createCell(i).setCellValue(headers.get(i));
        }
    }

    public void createFile(LinkedHashMap<String, String> data, Row row, int headerSize) {
        int pos = 0;
        Cell cell;

        while (pos < headerSize) {
            for (Map.Entry<String, String> dt : data.entrySet()) {
                cell = row.createCell(pos);
                cell.setCellValue(dt.getValue());
                pos++;
            }
        }
    }

    public byte[] saveFile(Workbook workbook, int row) {
        log.info("Guardando archivo excel con {} filas", row);

        try (ByteArrayOutputStream ou = new ByteArrayOutputStream()) {
            workbook.write(ou);
            return ou.toByteArray();
        } catch (Exception e) {
            log.error("Error al crear el archivo error.");
            throw new RuntimeException(e);
        }
    }
}
