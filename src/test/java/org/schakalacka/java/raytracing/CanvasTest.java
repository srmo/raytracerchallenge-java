package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CanvasTest {

    @Test
    void canvas() {
        var canvas = new Canvas(10, 20);

        assertEquals(10, canvas.getWidth());
        assertEquals(20, canvas.getHeight());

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                assertEquals(Color.BLACK, canvas.read(x, y));

            }
        }

    }

    @Test
    void writePixel() {
        var colorRed = new Color(1, 0, 0);
        var canvas = new Canvas(10, 20);
        canvas.write(2, 3, colorRed);

        assertEquals(colorRed, canvas.read(2, 3));
    }

}