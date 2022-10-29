package org.schakalacka.java.raytracing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PPMExporter {


    private final Canvas c;
    private final int numColors;
    private final FileWriter fw;
    private final BufferedWriter bw;

    private String lineToWrite = "";

    private PPMExporter(Canvas c, Path exportFileName, int numColors) throws IOException {

        this.c = c;
        this.numColors = numColors;
        this.fw = new FileWriter(exportFileName.toFile());
        this.bw = new BufferedWriter(fw);
    }

    public static void export(Canvas c, Path exportFileName, int numColors) throws IOException {
        PPMExporter ppmExporter = new PPMExporter(c, exportFileName, numColors);
        ppmExporter.export();
    }

    private void export() throws IOException {

        int width = c.getWidth();
        int height = c.getHeight();

        try (fw; bw) {
            append("P3");
            newLine();
            append(width + " " + height);
            newLine();
            append(String.valueOf(numColors));
            newLine();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final Color color = c.read(x, y);
                    String red = String.valueOf(color.rs(numColors));
                    String green = String.valueOf(color.gs(numColors));
                    String blue = String.valueOf(color.bs(numColors));
                    append(red);
                    append(" ");
                    append(green);
                    append(" ");
                    append(blue);
                    append(" ");

                }
                newLine(); // newline here is crucial. As only newLine() will actually write something out into the writer
            }
        }

    }

    private void append(String string) throws IOException {
        int maxLineLength = 70;
        if (lineToWrite.length() + string.trim().length() > maxLineLength) {
            newLine();
            lineToWrite = string.stripLeading(); // a new line shall not start with whitespace
        } else {
            lineToWrite = lineToWrite.concat(string);
        }
    }

    private void newLine() throws IOException {
        int length = lineToWrite.trim().length();
        if (length > 0 && length <= 70) {
            bw.write(lineToWrite.trim());
            lineToWrite = "";
        }
        bw.newLine();
    }

}
