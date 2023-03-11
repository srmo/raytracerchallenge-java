package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UV;
import org.schakalacka.java.raytracing.math.UVMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UVMappingTest {

    @Test
    void sphericalMapping() {
        Tuple point1 = Tuple.point(0, 0, -1);
        Tuple point2 = Tuple.point(1, 0, 0);
        Tuple point3 = Tuple.point(0, 0, 1);
        Tuple point4 = Tuple.point(-1, 0, 0);
        Tuple point5 = Tuple.point(0, 1, 0);
        Tuple point6 = Tuple.point(0, -1, 0);
        Tuple point7 = Tuple.point((float) (Math.sqrt(2) / 2), (float) (Math.sqrt(2) / 2), 0);


        Assertions.assertEquals(new UV(0.0f, 0.5f), (new UVMapping()).sphericalMap(point1));
        assertEquals(new UV(0.25f, 0.5f), (new UVMapping()).sphericalMap(point2));
        assertEquals(new UV(0.5f, 0.5f), (new UVMapping()).sphericalMap(point3));
        assertEquals(new UV(0.75f, 0.5f), (new UVMapping()).sphericalMap(point4));
        assertEquals(new UV(0.5f, 1.0f), (new UVMapping()).sphericalMap(point5));
        assertEquals(new UV(0.5f, 0.0f), (new UVMapping()).sphericalMap(point6));
        assertEquals(new UV(0.25f, 0.75f), (new UVMapping()).sphericalMap(point7));
    }

}