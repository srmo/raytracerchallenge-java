package org.schakalacka.java.raytracing.geometry.tracing;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Material;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.schakalacka.java.raytracing.Constants.SHAPE_POINT_OFFSET_EPSILON;

class PrecalcTest {

    @Test
    void precalc() {
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);

        assertEquals(4, precalc.getDistance());
        assertSame(sphere, precalc.getObject());
        assertEquals(Tuple.point(0,0,-1), precalc.getPoint());
        assertEquals(Tuple.vector(0,0,-1), precalc.getEyeVector());
        assertEquals(Tuple.vector(0,0,-1), precalc.getNormalVector());
    }

    @Test
    void precalcReflectionVector() {
        var ray = new Ray(Tuple.point(0,1,-1), Tuple.vector(0, (float) -(Math.sqrt(2)/2), (float) Math.sqrt(2)/2));
        var plane = new Plane();
        var intersection = new Intersection(plane, (float) Math.sqrt(2));

        var precalc = new Precalc(intersection, ray);

        assertEquals(Tuple.vector(0, (float) Math.sqrt(2)/2, (float) Math.sqrt(2)/2), precalc.getReflectVector());
    }

    @Test
    void hitOutside() {
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);
        assertFalse(precalc.inside());

    }

    @Test
    void hitInside() {
        var ray = new Ray(Tuple.point(0,0,0), Tuple.vector(0,0,1));
        var sphere = new Sphere();
        var intersection = new Intersection(sphere, 4);

        var precalc = new Precalc(intersection, ray);
        assertTrue(precalc.inside());

    }

    @Test
    void hitShouldOffsetPoint() {
        // this is to remove "acne". Floating point math is ever so slightly inaccurate, due to implicit rounding errors
        // we try and figure out cases where this might happen, and offset a surface point slightly above the "actual" surface
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));
        var shape = new Sphere();
        shape.setTransformationMatrix(MatrixProvider.translation(0,0,1));

        var intersection = new Intersection(shape, 5);

        var precalc = new Precalc(intersection, ray);

        assertTrue(precalc.getOverPoint().z() < -SHAPE_POINT_OFFSET_EPSILON /2);
        assertTrue(precalc.getPoint().z() > precalc.getOverPoint().z());

    }


    @Test
    void refractiveIndices() {
        var glassSphereA = Sphere.glassySphere();
        glassSphereA.setMaterial(Material.newMaterial().refractiveIndex(1.5f).create());
        glassSphereA.setTransformationMatrix(MatrixProvider.scaling(2,2,2));

        var glassSphereB = Sphere.glassySphere();
        glassSphereB.setMaterial(Material.newMaterial().refractiveIndex(2.0f).create());
        glassSphereB.setTransformationMatrix(MatrixProvider.translation(0,0,-0.25f));

        var glassSphereC = Sphere.glassySphere();
        glassSphereC.setMaterial(Material.newMaterial().refractiveIndex(2.5f).create());
        glassSphereC.setTransformationMatrix(MatrixProvider.translation(0,0,0.25f));

        var ray = new Ray(Tuple.point(0,0,-4), Tuple.vector(0,0,1));
        var intersections = new ArrayList<Intersection>();
        intersections.add(new Intersection(glassSphereA, 2));
        intersections.add(new Intersection(glassSphereB, 2.75f));
        intersections.add(new Intersection(glassSphereC, 3.25f));
        intersections.add(new Intersection(glassSphereB, 4.75f));
        intersections.add(new Intersection(glassSphereC, 5.25f));
        intersections.add(new Intersection(glassSphereA, 6));

        // expected values
        var expected = new ArrayList<Tuple>();
        expected.add(Tuple.vector(1.0f,1.5f,0));
        expected.add(Tuple.vector(1.5f,2.0f,0));
        expected.add(Tuple.vector(2.0f,2.5f,0));
        expected.add(Tuple.vector(2.5f,2.5f,0));
        expected.add(Tuple.vector(2.5f,1.5f,0));
        expected.add(Tuple.vector(1.5f,1.0f,0));


        var precalc = new Precalc(intersections.get(0), ray, intersections);
        var n1 = precalc.getN1();
        var n2 = precalc.getN2();
        assertEquals(expected.get(0).x(), n1);
        assertEquals(expected.get(0).y(), n2);

         precalc = new Precalc(intersections.get(1), ray, intersections);
         n1 = precalc.getN1();
         n2 = precalc.getN2();
        assertEquals(expected.get(1).x(), n1);
        assertEquals(expected.get(1).y(), n2);

        precalc = new Precalc(intersections.get(2), ray, intersections);
        n1 = precalc.getN1();
        n2 = precalc.getN2();
        assertEquals(expected.get(2).x(), n1);
        assertEquals(expected.get(2).y(), n2);

        precalc = new Precalc(intersections.get(3), ray, intersections);
        n1 = precalc.getN1();
        n2 = precalc.getN2();
        assertEquals(expected.get(3).x(), n1);
        assertEquals(expected.get(3).y(), n2);

        precalc = new Precalc(intersections.get(4), ray, intersections);
        n1 = precalc.getN1();
        n2 = precalc.getN2();
        assertEquals(expected.get(4).x(), n1);
        assertEquals(expected.get(4).y(), n2);

        precalc = new Precalc(intersections.get(5), ray, intersections);
        n1 = precalc.getN1();
        n2 = precalc.getN2();
        assertEquals(expected.get(5).x(), n1);
        assertEquals(expected.get(5).y(), n2);

    }

    @Test
    void underPoint() {
        var shape = Sphere.glassySphere();
        shape.setTransformationMatrix(MatrixProvider.translation(0,0,1));
        var ray = new Ray(Tuple.point(0,0,-5), Tuple.vector(0,0,1));

        var intersection = new Intersection(shape, 5);
        var intersections = Collections.singletonList(intersection);

        var precalc = new Precalc(intersection, ray, intersections);

        assertTrue(precalc.getUnderPoint().z() > SHAPE_POINT_OFFSET_EPSILON / 2);
        assertTrue(precalc.getPoint().z() < precalc.getUnderPoint().z());
    }

    @Test
    void schlickTotalInternalReflection() {
        var shape = Sphere.glassySphere();
        var ray = new Ray(Tuple.point(0,0, (float) (Math.sqrt(2)/2)), Tuple.vector(0,1,0));
        var intersections = new ArrayList<Intersection>();
        intersections.add(new Intersection(shape, (float) (-Math.sqrt(2)/2)));
        intersections.add(new Intersection(shape, (float) (Math.sqrt(2)/2)));

        var precalc = new Precalc(intersections.get(1), ray, intersections);
        assertEquals(1.0f, precalc.schlick());

    }

    @Test
    void schlickPerpendicularRay() {
        var shape = Sphere.glassySphere();
        var ray = new Ray(Tuple.point(0,0,0), Tuple.vector(0,1,0));
        var intersections = new ArrayList<Intersection>();
        intersections.add(new Intersection(shape, -1));
        intersections.add(new Intersection(shape, 1));

        var precalc = new Precalc(intersections.get(1), ray, intersections);
        assertEquals(0.04f, precalc.schlick(), Constants.EQUALS_EPSILON);

    }

    @Test
    void schlickForSmallAngle() {
        var shape = Sphere.glassySphere();
        var ray = new Ray(Tuple.point(0,0.99f,-2), Tuple.vector(0,0,1));
        var intersections = new ArrayList<Intersection>();
        intersections.add(new Intersection(shape, 1.8589f));

        var precalc = new Precalc(intersections.get(0), ray, intersections);
        assertEquals(0.48873f, precalc.schlick(), Constants.EQUALS_EPSILON);

    }
}