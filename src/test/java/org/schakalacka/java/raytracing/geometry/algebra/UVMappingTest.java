package org.schakalacka.java.raytracing.geometry.algebra;

import org.junit.jupiter.api.Test;

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
        Tuple point7 = Tuple.point(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);


        assertEquals(new UV(0.0, 0.5), (new UVMapping()).sphericalMap(point1));
        assertEquals(new UV(0.25, 0.5), (new UVMapping()).sphericalMap(point2));
        assertEquals(new UV(0.5, 0.5), (new UVMapping()).sphericalMap(point3));
        assertEquals(new UV(0.75, 0.5), (new UVMapping()).sphericalMap(point4));
        assertEquals(new UV(0.5, 1.0), (new UVMapping()).sphericalMap(point5));
        assertEquals(new UV(0.5, 0.0), (new UVMapping()).sphericalMap(point6));
        assertEquals(new UV(0.25, 0.75), (new UVMapping()).sphericalMap(point7));
    }

}