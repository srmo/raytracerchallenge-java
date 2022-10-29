package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PPMExporterTest {

    @Test
    void exportCanvas() throws IOException {
        var canvas = new Canvas(5, 3);
        canvas.write(0, 0, new Color(1.5, 0, 0));
        canvas.write(2, 1, new Color(0, 0.5, 0));
        canvas.write(4, 2, new Color(-0.5, 0, 1));

        Path exportFileName = Path.of("export.ppm");
        PPMExporter.export(canvas, exportFileName, 255);

        try (FileReader fr = new FileReader(exportFileName.toFile()); BufferedReader br = new BufferedReader(fr)) {
            var line = br.readLine();
            assertEquals("P3", line);
            line = br.readLine();
            assertEquals("5 3", line);
            line = br.readLine();
            assertEquals("255", line);
            line = br.readLine();
            assertEquals("255 0 0 0 0 0 0 0 0 0 0 0 0 0 0", line.trim());
            line = br.readLine();
            assertEquals("0 0 0 0 0 0 0 128 0 0 0 0 0 0 0", line.trim());
            line = br.readLine();
            assertEquals("0 0 0 0 0 0 0 0 0 0 0 0 0 0 255", line.trim());
        }
    }

    @Test
    void foo() {

        long lines = 0;
        Path exportFileName = Path.of("export.ppm");
        try (BufferedReader reader = new BufferedReader(new FileReader(exportFileName.toFile()))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);

    }

    @Test
    void exportLongLines() throws IOException {
        var canvas = new Canvas(10, 2, new Color(1, 0.8, 0.6));

        Path exportFileName = Path.of("export.ppm");
        PPMExporter.export(canvas, exportFileName, 255);

        try (FileReader fr = new FileReader(exportFileName.toFile()); BufferedReader br = new BufferedReader(fr)) {
            var line = br.readLine();
            assertEquals("P3", line);
            line = br.readLine();
            assertEquals("10 2", line);
            line = br.readLine();
            assertEquals("255", line);
            line = br.readLine();
            assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", line.trim());
            line = br.readLine();
            assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", line.trim());
            line = br.readLine();
            assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", line.trim());
            line = br.readLine();
            assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", line.trim());
            line = br.readLine();
            assertNull(line);
        }
    }

    @Test
    void newLineAtEnd() throws IOException {
        var canvas = new Canvas(10, 2, new Color(1, 0.8, 0.6));

        Path exportFileName = Path.of("export.ppm");
        PPMExporter.export(canvas, exportFileName, 255);

        RandomAccessFile fileHandler = new RandomAccessFile(exportFileName.toFile(), "r");
        long fileLength = fileHandler.length() - 1;

        fileHandler.seek(fileLength);
        byte readByte = fileHandler.readByte();
        fileHandler.close();
        assertTrue(readByte == 0xA || readByte == 0xD);
    }


}