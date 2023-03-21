package org.schakalacka.java.raytracing.geometry.objects;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CubeTest {

    @Test
    void testCubeIntersection() {
        var cube = new Cube();

        List<Tuple> origins = new ArrayList<>();
        List<Tuple> directions = new ArrayList<>();
        List<Double> t1s = new ArrayList<>();
        List<Double> t2s = new ArrayList<>();

        origins.add(Tuple.point(5, .5, 0));
        origins.add(Tuple.point(-5, .5, 0));
        origins.add(Tuple.point(.5, 5, 0));
        origins.add(Tuple.point(.5, -5, 0));
        origins.add(Tuple.point(.5, 0, 5));
        origins.add(Tuple.point(.5, 0, -5));
        origins.add(Tuple.point(0, 0.5, 0));
        directions.add(Tuple.vector(-1, 0, 0));
        directions.add(Tuple.vector(1, 0, 0));
        directions.add(Tuple.vector(0, -1, 0));
        directions.add(Tuple.vector(0, 1, 0));
        directions.add(Tuple.vector(0, 0, -1));
        directions.add(Tuple.vector(0, 0, 1));
        directions.add(Tuple.vector(0, 0, 1));
        t1s.add(4.0);
        t1s.add(4.0);
        t1s.add(4.0);
        t1s.add(4.0);
        t1s.add(4.0);
        t1s.add(4.0);
        t1s.add(-1.0);
        t2s.add(6.0);
        t2s.add(6.0);
        t2s.add(6.0);
        t2s.add(6.0);
        t2s.add(6.0);
        t2s.add(6.0);
        t2s.add(1.0);

        for (int i = 0; i < origins.size(); i++) {
            var origin = origins.get(i);
            var direction = directions.get(i);
            var ray = new Ray(origin, direction);
            var xs = cube.localIntersect(ray);
            assertEquals(2, xs.size());
            assertEquals(t1s.get(i), xs.get(0).getDistance());
            assertEquals(t2s.get(i), xs.get(1).getDistance());
            }
        }

    @Test
    void rayMissesCube() {
        var cube = new Cube();

        List<Tuple> origins = new ArrayList<>();
        List<Tuple> directions = new ArrayList<>();

        origins.add(Tuple.point(-2, 0, 0));
        origins.add(Tuple.point(0, -2, 0));
        origins.add(Tuple.point(0, 0, -2));
        origins.add(Tuple.point(2, 0, 2));
        origins.add(Tuple.point(0, 2, 2));
        origins.add(Tuple.point(2, 2, 0));
        directions.add(Tuple.vector(0.2673, 0.5345, 0.8018));
        directions.add(Tuple.vector(0.8018, 0.2673, 0.5345));
        directions.add(Tuple.vector(0.5345, 0.8018, 0.2673));
        directions.add(Tuple.vector(0, 0, -1));
        directions.add(Tuple.vector(0, -1, 0));
        directions.add(Tuple.vector(-1, 0, 0));

        for (int i = 0; i < origins.size(); i++) {
            var origin = origins.get(i);
            var direction = directions.get(i);
            var ray = new Ray(origin, direction);
            var xs = cube.localIntersect(ray);
            assertEquals(0, xs.size());
        }
    }

    @Test
    void normal() {
        var cube = new Cube();

        var point = Tuple.point(1, 0.5, -0.8);
        assertEquals(Tuple.vector(1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-1, -0.2, 0.9);
        assertEquals(Tuple.vector(-1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-0.4, 1, -0.1);
        assertEquals(Tuple.vector(0, 1, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(0.3, -1, -0.7);
        assertEquals(Tuple.vector(0, -1, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-0.6, 0.3, 1);
        assertEquals(Tuple.vector(0, 0, 1), cube.localNormalVectorAt(point));

        point = Tuple.point(0.4, 0.4, -1);
        assertEquals(Tuple.vector(0, 0, -1), cube.localNormalVectorAt(point));

        point = Tuple.point(1, 1, 1);
        assertEquals(Tuple.vector(1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-1, -1, -1);
        assertEquals(Tuple.vector(-1, 0, 0), cube.localNormalVectorAt(point));

    }


    @Test
    void bounds() {
        var cube = new Cube();
        var bounds = cube.getBounds();
        assertEquals(Tuple.point(-1, -1, -1), bounds.getLower());
        assertEquals(Tuple.point(1, 1, 1), bounds.getUpper());
    }
}