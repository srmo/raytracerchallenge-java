package org.schakalacka.java.raytracing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void color() {
        var color = new Color(0.1,0.2,0.3);

        assertEquals(0.1, color.r());
        assertEquals(0.2, color.g());
        assertEquals(0.3, color.b());
    }

    @Test
    void scaleValue() {
        assertEquals(128, Color.scaleValue(0.5, 255));
        assertEquals(0, Color.scaleValue(-10.5, 255));
        assertEquals(255, Color.scaleValue(120.5, 255));

        var color = new Color(-1,0.5,10);

        assertEquals(0, color.rs(255));
        assertEquals(128, color.gs(255));
        assertEquals(255, color.bs(255));
    }

    @Test
    void addColor() {
        var color1 = new Color(0.9, 0.6, 0.75);
        var color2 = new Color(0.7, 0.1, 0.25);

        var result = color1.add(color2);
        var expected = new Color(1.6, 0.7, 1);

        assertEquals(expected, result);

    }

    @Test
    void subtractColor() {
        var color1 = new Color(0.9, 0.6, 0.75);
        var color2 = new Color(0.7, 0.1, 0.25);

        var result = color1.sub(color2);
        var expected = new Color(0.2, 0.5, 0.5);

        assertEquals(expected, result);
    }

    @Test
    void multiplyScalar() {
        var color1 = new Color(0.9, 0.6, 0.75);

        var result = color1.mulS(2);
        var expected = new Color(1.8, 1.2, 1.5);

        assertEquals(expected, result);
    }

    @Test
    void multiplyColor() {
        var color1 = new Color(1,0.2,0.4);
        var color2 = new Color(0.9,1,0.1);

        var result = color1.mulC(color2);
        var expected = new Color(0.9,0.2,0.04);

        assertEquals(expected, result);
    }

}