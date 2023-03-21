package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.Tuple;

import static org.junit.jupiter.api.Assertions.*;

class BoundsTest {

    @Test
    void boundsHaveUpperAndLowerBounds() {
        var bounds = new Bounds(Tuple.point(1, 2, 3), Tuple.point(4, 5, 6));
        assertEquals(Tuple.point(1,2,3), bounds.getLower());
        assertEquals(Tuple.point(4,5,6), bounds.getUpper());
    }


}