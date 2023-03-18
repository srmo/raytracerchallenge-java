package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.MATRIX_TYPE;
import org.schakalacka.java.raytracing.math.Matrix;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.cublas.RayTracingCublas;
import org.schakalacka.java.raytracing.scene.tools.Chunk;
import org.schakalacka.java.raytracing.scene.tools.ChunkCalculator;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Camera {


    private final int hSize;
    private final int vSize;
    private final double fieldOfView;
    private Matrix transformationMatrix;
    private double pixelSize;
    private double halfWidth;
    private double halfHeight;
    private Matrix inverseTransformationMatrix;

    public Camera(int hSize, int vSize, double fieldOfView) {

        this.hSize = hSize;
        this.vSize = vSize;
        this.fieldOfView = fieldOfView;
        setTransformationMatrix(MatrixProvider.get(4, true));
        calculatePixelSize();
    }

    private void calculatePixelSize() {
        double halfView = Math.tan(fieldOfView / 2);
        double aspectRatio = (double) hSize /  vSize;

        if (aspectRatio >= 1) {
            halfWidth = halfView;
            halfHeight = halfView / aspectRatio;
        } else {
            halfWidth = halfView * aspectRatio;
            halfHeight = halfView;
        }
        pixelSize = (halfWidth * 2) / hSize;
    }

    public int getHSize() {
        return hSize;
    }

    public int getVSize() {
        return vSize;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void setTransformationMatrix(Matrix matrix) {
        this.transformationMatrix = matrix;
        this.inverseTransformationMatrix = transformationMatrix.inverse();
    }

    public double getPixelSize() {
        return pixelSize;
    }

    public Ray rayForPixel(int px, int py) {
        // offset from the edge of the canvas to the pixel's center
        double xOffset = (px + 0.5) * pixelSize;
        double yOffset = (py + 0.5) * pixelSize;

        // untransformed coordinates of the pixel in world space
        // (the camera looks toward -z, so +x is to the *left*)
        var worldX = halfWidth - xOffset;
        var worldY = halfHeight - yOffset;


        var pixel = inverseTransformationMatrix.mulT(Tuple.point((float) worldX, (float) worldY, -1));
        var origin = inverseTransformationMatrix.mulT(Tuple.point(0, 0, 0));
        var direction = pixel.sub(origin).normalize();

        return new Ray(origin, direction);
    }

    public Canvas render(World world) {
        return this.render(world, 1);
    }

    @SuppressWarnings("unused")
    public Canvas renderSinglePixel(World world, int x, int y) {
        var image = new Canvas(this.hSize, this.vSize);
        var ray = this.rayForPixel(x, y);
        var color = world.color_at(ray);
        image.write(x, y, color);
        return image;
    }

    public Canvas render(World world, int parallelChunks) {
        if (Objects.requireNonNull(MatrixProvider.MT) == MATRIX_TYPE.CUBLAS) {
            RayTracingCublas.setupContext();
        }

        try {
            var image = new Canvas(this.hSize, this.vSize);

            Chunk[] chunks = ChunkCalculator.calculateChunks(parallelChunks, hSize, vSize);

            ForkJoinPool customThreadPool = new ForkJoinPool(parallelChunks);
            try {

                customThreadPool.submit(() -> Arrays.asList(chunks).parallelStream().forEach(chunk -> {
                    Logger.info("starting chunk {}", chunk);
                    var xFrom = chunk.xFrom();
                    var xTo = chunk.xTo();
                    var yFrom = chunk.yFrom();
                    var yTo = chunk.yTo();

                    for (int y = yFrom; y <= yTo; y++) {
                        for (int x = xFrom; x <= xTo; x++) {
                            var ray = this.rayForPixel(x, y);
                            var color = world.color_at(ray);
                            image.write(x, y, color);
                        }
                    }
                })).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                customThreadPool.shutdown();
            }

            return image;
        } finally {
            if (Objects.requireNonNull(MatrixProvider.MT) == MATRIX_TYPE.CUBLAS) {
                RayTracingCublas.destroyContext();
            }
        }
    }

}
