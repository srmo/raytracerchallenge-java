package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.tracing.IntersectionTracker;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Canvas;
import org.schakalacka.java.raytracing.scene.Color;
import org.tinylog.Logger;

public class Chapter5 {


    public static void main(String[] args) {

        var rayOrigin = Tuple.point(0, 0, -2);

        var wallZ = 100; // this is also related to the length of our ray. From it's starting point till it hits the wall/canvas
        // this is based on the idea, that a tangent ray for a given wallZ needs space.
        // Consider the ray always starting at x0y0 so it has some angle that needs to hit the wall, in the end
        double wallSize = 500;
        double canvasSize = 500;

        // still, I have no idea of the concept of world vs object space. but here is a conversion, as suggested by the book
        double pixelSize = wallSize / canvasSize;
        double half = wallSize / 2;

        var canvas = new Canvas((int) canvasSize, (int) canvasSize);
        var red = new Color(1, 0, 0);
        var sphere = new Sphere();
        sphere.setTransformationMatrix(MatrixProvider.shearing(1, 0, 0, 0, 0, 0).mulM(MatrixProvider.scaling(0.5f, 1, 1)));

        long renderStart = System.currentTimeMillis();
        for (int y = 0; y < canvasSize; y++) {
            // y! top = +half, bottom = -half
            // so here we start from the top
            int worldY = (int) (half - pixelSize * y);

            for (int x = 0; x < canvasSize; x++) {
                // x! left = -half, right = half
                // so here we start on the left
                int worldX = (int) (-half + pixelSize * x);

                // a point on the wall, where the ray should hit
                var targetPosition = Tuple.point(worldX, worldY, wallZ);
                var ray = new Ray(rayOrigin, targetPosition.sub(rayOrigin).normalize());
                var intersections = sphere.intersect(ray);
                if (IntersectionTracker.getHit(intersections) != null) {
                    canvas.write(x, y, red);
                }
            }
        }
        long renderEnd = System.currentTimeMillis();
        Logger.info("Rendertime: {}ms", (renderEnd - renderStart));


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter5_sphereShadow.ppm", 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
