package org.schakalacka.java.raytracing.scene;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CameraTest {

    @Test
    void createCamera() {
        var hSize = 160;
        var vSize = 120;
        var fieldOfView = Math.PI / 2;
        var camera = new Camera(hSize, vSize, fieldOfView);

        assertEquals(160, camera.getHSize());
        assertEquals(120, camera.getVSize());
        assertEquals(Math.PI / 2, camera.getFieldOfView());
        assertEquals(MatrixProvider.get(4, true), camera.getTransformationMatrix());
    }

    @Test
    void pixelSizeForHorizontalCanvas() {
        var camera = new Camera(200, 125, Math.PI / 2);

        assertEquals(0.01, camera.getPixelSize(), Constants.EQUALS_EPSILON);
    }

    @Test
    void pixelSizeForVerticalCanvas() {
        var camera = new Camera(125, 200, Math.PI / 2);

        assertEquals(0.01, camera.getPixelSize(), Constants.EQUALS_EPSILON);
    }

    @Test
    void rayThroughCenterOfCanvas() {
        var camera = new Camera(201, 101, Math.PI / 2);

        var ray = camera.rayForPixel(100, 50);

        assertEquals(Tuple.point(0, 0, 0), ray.origin());
        assertEquals(Tuple.vector(0, 0, -1), ray.direction());
    }

    @Test
    void rayThroughCornerOfCanvas() {
        var camera = new Camera(201, 101, Math.PI / 2);

        var ray = camera.rayForPixel(0, 0);

        assertEquals(Tuple.point(0, 0, 0), ray.origin());
        assertEquals(Tuple.vector(0.66519f, 0.33259f,  -0.66851), ray.direction());

    }

    @Test
    void rayForTransformedCamera() {
        var camera = new Camera(201, 101, Math.PI / 2);
        camera.setTransformationMatrix(MatrixProvider.rotationY( (Math.PI / 4)).mulM(MatrixProvider.translation(0, -2, 5)));

        var ray = camera.rayForPixel(100, 50);

        assertEquals(Tuple.point(0, 2, -5), ray.origin());
        assertEquals(Tuple.vector( (Math.sqrt(2) / 2), 0,  (-Math.sqrt(2) / 2)), ray.direction());

    }

    @Test
    void renderWorldWithCamera() {
        var world = World.getDefault();
        var camera = new Camera(11, 11, Math.PI / 2);
        var from = Tuple.point(0, 0, -5);
        var to = Tuple.point(0, 0, 0);
        var up = Tuple.vector(0, 1, 0);
        camera.setTransformationMatrix(ViewTransformation.transform(from, to, up));

        Canvas renderedCanvas = camera.render(world);

        assertEquals(new Color(0.38066, 0.47583, 0.2855), renderedCanvas.read(5, 5));
    }
}