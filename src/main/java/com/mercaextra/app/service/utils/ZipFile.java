package com.mercaextra.app.service.utils;

import com.mercaextra.app.service.impl.CajaServiceImpl;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipFile {

    private final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    public static byte[] fileComprime(Map<String, byte[]> files) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(bos))) {
            for (Map.Entry<String, byte[]> file : files.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(file.getKey()));
                zipOut.write(file.getValue());
                zipOut.flush();
            }
        }

        byte[] compressed = bos.toByteArray();
        try (
            FileOutputStream fileOutputStream = new FileOutputStream(
                "C:\\Users\\seortegon\\Downloads\\" + System.currentTimeMillis() + ".zip"
            )
        ) {
            fileOutputStream.write(compressed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bos.close();

        return compressed;
    }
}
