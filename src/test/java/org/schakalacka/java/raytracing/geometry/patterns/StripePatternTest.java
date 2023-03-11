package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StripePatternTest {


    @Test
    void stripePattern() {
        var pattern = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.a());
        assertEquals(Color.BLACK, pattern.b());
    }

    @Test
    void stripePatternConstantInY() {
        var pattern = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 1, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 2, 0)));
    }

    @Test
    void stripePatternConstantInZ() {
        var pattern = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 1)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 2)));
    }

    @Test
    void stripePatternAlternatesWithX() {
        var pattern = new StripePattern(Color.WHITE, Color.BLACK);

        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(0.9f, 0, 0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(1, 0, 0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(-0.1f, 0, 0)));
        assertEquals(Color.BLACK, pattern.patternAt(Tuple.point(-1, 0, 0)));
        assertEquals(Color.WHITE, pattern.patternAt(Tuple.point(-1.1f, 0, 0)));
    }

    @Test
    void stripesWithObjectTransform() {
        var sphere = new Sphere();
        sphere.setTransformationMatrix(MatrixProvider.scaling(2,2,2));

        var pattern = new StripePattern(Color.WHITE, Color.BLACK);
        Color color = pattern.patternAtShape(sphere, Tuple.point(1.5f, 0, 0));
        assertEquals(Color.WHITE, color);

    }

    @Test
    void stripesWithPatternTransform() {
        var sphere = new Sphere();

        StripePattern pattern = new StripePattern(Color.WHITE, Color.BLACK);
        pattern.setTransformationMatrix(MatrixProvider.scaling(2,2,2));

        Color color = pattern.patternAtShape(sphere, Tuple.point(1.5f, 0, 0));
        assertEquals(Color.WHITE, color);

    }

    @Test
    void stripesWithPatternAndObjectTransform() {
        var sphere = new Sphere();
        sphere.setTransformationMatrix(MatrixProvider.scaling(2,2,2));

        var pattern = new StripePattern(Color.WHITE, Color.BLACK);
        pattern.setTransformationMatrix(MatrixProvider.translation(0.5f,0,0));

        Color color = pattern.patternAtShape(sphere, Tuple.point(2.5f, 0, 0));
        assertEquals(Color.WHITE, color);

    }

}