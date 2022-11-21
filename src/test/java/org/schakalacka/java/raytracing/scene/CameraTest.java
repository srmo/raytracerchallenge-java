package org.schakalacka.java.raytracing.scene;

import org.junit.jupiter.api.Test;
import org.schakalacka.java.raytracing.Constants;
import org.schakalacka.java.raytracing.geometry.Matrix;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    @Test
    void createCamera() {
        var hsize = 160;
        var vsize = 120;
        var fieldOfView = Math.PI/2;
        var camera = new Camera(hsize, vsize, fieldOfView);

        assertEquals(160, camera.getHsize());
        assertEquals(120, camera.getVsize());
        assertEquals(Math.PI/2, camera.getFieldOfView());
        assertEquals(Matrix.get(4, true), camera.getTransformationMatrix());
    }

    @Test
    void pixelSizeForHorizontalCanvas() {
        var camera = new Camera(200,125,Math.PI/2);

        assertEquals(0.01, camera.getPixelSize(), Constants.EPSILON);
    }

    @Test
    void pixelSizeForVerticalCanvas() {
        var camera = new Camera(125,200,Math.PI/2);

        assertEquals(0.01, camera.getPixelSize(), Constants.EPSILON);
    }


}