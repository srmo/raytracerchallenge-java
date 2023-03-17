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
        List<Float> t1s = new ArrayList<>();
        List<Float> t2s = new ArrayList<>();

        origins.add(Tuple.point(5, .5f, 0));
        origins.add(Tuple.point(-5, .5f, 0));
        origins.add(Tuple.point(.5f, 5, 0));
        origins.add(Tuple.point(.5f, -5, 0));
        origins.add(Tuple.point(.5f, 0, 5));
        origins.add(Tuple.point(.5f, 0, -5));
        origins.add(Tuple.point(0, 0.5f, 0));
        directions.add(Tuple.vector(-1, 0, 0));
        directions.add(Tuple.vector(1, 0, 0));
        directions.add(Tuple.vector(0, -1, 0));
        directions.add(Tuple.vector(0, 1, 0));
        directions.add(Tuple.vector(0, 0, -1));
        directions.add(Tuple.vector(0, 0, 1));
        directions.add(Tuple.vector(0, 0, 1));
        t1s.add(4f);
        t1s.add(4f);
        t1s.add(4f);
        t1s.add(4f);
        t1s.add(4f);
        t1s.add(4f);
        t1s.add(-1f);
        t2s.add(6f);
        t2s.add(6f);
        t2s.add(6f);
        t2s.add(6f);
        t2s.add(6f);
        t2s.add(6f);
        t2s.add(1f);

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
        directions.add(Tuple.vector(0.2673f, 0.5345f, 0.8018f));
        directions.add(Tuple.vector(0.8018f, 0.2673f, 0.5345f));
        directions.add(Tuple.vector(0.5345f, 0.8018f, 0.2673f));
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

        var point = Tuple.point(1, 0.5f, -0.8f);
        assertEquals(Tuple.vector(1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-1, -0.2f, 0.9f);
        assertEquals(Tuple.vector(-1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-0.4f, 1, -0.1f);
        assertEquals(Tuple.vector(0, 1, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(0.3f, -1, -0.7f);
        assertEquals(Tuple.vector(0, -1, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-0.6f, 0.3f, 1);
        assertEquals(Tuple.vector(0, 0, 1), cube.localNormalVectorAt(point));

        point = Tuple.point(0.4f, 0.4f, -1);
        assertEquals(Tuple.vector(0, 0, -1), cube.localNormalVectorAt(point));

        point = Tuple.point(1, 1, 1);
        assertEquals(Tuple.vector(1, 0, 0), cube.localNormalVectorAt(point));

        point = Tuple.point(-1, -1, -1);
        assertEquals(Tuple.vector(-1, 0, 0), cube.localNormalVectorAt(point));

    }
}