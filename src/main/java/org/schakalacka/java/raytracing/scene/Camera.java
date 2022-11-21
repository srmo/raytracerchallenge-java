package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.geometry.Matrix;

public class Camera {


    private final int hsize;
    private final int vsize;
    private final double fieldOfView;
    private final Matrix transformationMatrix;

    private double pixelSize;
    private double halfWidth;
    private double halfHeight;

    public Camera(int hsize, int vsize, double fieldOfView) {

        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformationMatrix = Matrix.get(4, true);
        calculatePixelSize();
    }

    private void calculatePixelSize() {
        double halfView = Math.tan(fieldOfView / 2);
        double aspectRatio = (double) hsize / (double) vsize;

        if (aspectRatio >= 1) {
            halfWidth = halfView;
            halfHeight = halfView / aspectRatio;
        } else {
            halfWidth = halfView * aspectRatio;
            halfHeight = halfView;
        }
        pixelSize = (halfWidth * 2) / hsize;
    }

    public int getHsize() {
        return hsize;
    }

    public int getVsize() {
        return vsize;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }


    public double getPixelSize() {
        return pixelSize;
    }
}
