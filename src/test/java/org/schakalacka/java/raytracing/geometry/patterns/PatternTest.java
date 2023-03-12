package org.schakalacka.java.raytracing.geometry.patterns;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.scene.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternTest {

    @Test
    void defaultTransformation() {
        var pattern = new TestPattern();

        var matrix = pattern.getTransformationMatrix();

        assertEquals(MatrixProvider.get(4, true), matrix);
    }

    @Test
    void setTransformMatrixViaSetter() {
        var pattern = new TestPattern();
        pattern.setTransformationMatrix(MatrixProvider.translation(1, 2, 3));

        assertEquals(MatrixProvider.translation(1, 2, 3), pattern.getTransformationMatrix());
    }

    @Test
    void withObjectTransform() {
        var sphere = new Sphere();
        sphere.setTransformationMatrix(MatrixProvider.scaling(2, 2, 2));

        var pattern = new TestPattern();
        Color color = pattern.patternAtShape(sphere, Tuple.point(2, 3, 4));
        assertEquals(new Color(1, 1.5, 2), color);

    }

    @Test
    void stripesWithPatternTransform() {
        var sphere = new Sphere();

        TestPattern pattern = new TestPattern();
        pattern.setTransformationMatrix(MatrixProvider.scaling(2, 2, 2));

        Color color = pattern.patternAtShape(sphere, Tuple.point(2, 3, 4));
        assertEquals(new Color(1, 1.5, 2), color);

    }

    @Test
    void stripesWithPatternAndObjectTransform() {
        var sphere = new Sphere();
        sphere.setTransformationMatrix(MatrixProvider.scaling(2, 2, 2));

        var pattern = new TestPattern();
        pattern.setTransformationMatrix(MatrixProvider.translation(0.5f, 1, 1.5f));

        Color color = pattern.patternAtShape(sphere, Tuple.point(2.5f, 3, 3.5f));
        assertEquals(new Color(0.75, 0.5, 0.25), color);
    }

    /***
     * I'll follow the book here, b/c I find the idea interesting to use test-object specific fields that my tests can inspect
     */
    public static class TestPattern extends Pattern {

        @Override
        public Color patternAt(Tuple point) {
            return new Color(point.x(), point.y(), point.z());
        }

    }

}