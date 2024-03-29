package org.schakalacka.java.raytracing.scene;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.RTPoint;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.*;

class PointLightTest {

    @Test
    void letThereBeLight() {

        RTPoint position = Tuple.point(0, 0, 0);
        Color intensity = new Color(1, 1, 1);
        var light = new PointLight(position, intensity);

        assertEquals(position, light.position());
        assertEquals(intensity, light.intensity());
    }

}