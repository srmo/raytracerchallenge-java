package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.Matrix;
import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.tracing.IntersectionTracker;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.scene.Canvas;
import org.schakalacka.java.raytracing.scene.Color;
import org.schakalacka.java.raytracing.scene.Material;
import org.schakalacka.java.raytracing.scene.PointLight;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RenderSphere {


    public static void main(String[] args) {

        var rayOrigin = Tuple.point(0, 0, -5);

        var wallZ = 1000; // this is also related to the length of our ray. From it's starting point till it hits the wall/canvas
        // this is based on the idea, that a tangent ray for a given wallZ needs space.
        // Consider the ray always starting at x0y0 so it has some angle that needs to hit the wall, in the end
        double wallSize = 500;
        double canvasPixels = 500;

        // still, I have no idea of the concept of world vs object space. but here is a conversion, as suggested by the book
        double pixelSize = wallSize / canvasPixels;
        Logger.info("pixelSize = {}", pixelSize);
        double half = wallSize / 2;

        var canvas = new Canvas((int) canvasPixels, (int) canvasPixels);
        var red = new Color(1, 0, 0);
        var sphere = new Sphere();
        sphere.setTransformationMatrix(Matrix.shearing(1, 0, 0, 0, 0, 0).mulM(Matrix.scaling(0.5, 1, 1)));

        Material sphereMaterial = Material.newMaterial().color(new Color(1, 0.2, 1)).create();
        sphere.setMaterial(sphereMaterial);

        var lightSource = new PointLight(Tuple.point(-10, 10, -10), Color.WHITE);


        boolean renderParallel = true;

        long renderStart = System.currentTimeMillis();


        if (renderParallel) {
            IntStream.range(0, 5).parallel().forEach(yIndex -> {
                for (int y = (yIndex * 100); y < yIndex * 100 + 100; y++) {
                    int worldY = (int) (half - pixelSize * y);

                    final int currentY = y;
                    IntStream.range(0, 5).parallel().forEach(xIndex -> {
                        for (int x = (xIndex * 100); x < xIndex * 100 + 100; x++) {
                            // x! left = -half, right = half
                            // so here we start on the left
                            int worldX = (int) (-half + pixelSize * x);

                            // a point on the wall, where the ray should hit
                            var targetPosition = Tuple.point(worldX, worldY, wallZ);
                            var ray = new Ray(rayOrigin, targetPosition.sub(rayOrigin).normalize());
                            var intersections = sphere.intersect(ray);
                            var hit = IntersectionTracker.getHit(intersections);
                            if (hit != null) {
                                Tuple position = ray.position(hit.getDistance()); // the point where the ray hit the sphere
                                Tuple normalAtHit = hit.getIntersectedObject().normal(position); // the normal at the point where the ray hit the sphere
                                var eyeVector = ray.direction().normalize().negate();
                                var colorToPaint = sphereMaterial.lighting(lightSource, position, eyeVector, normalAtHit);
                                canvas.write(x, currentY, colorToPaint);
                            }
                        }
                    });
                }
            });
        } else {

            for (int y = 0; y < canvasPixels; y++) {
                // y! top = +half, bottom = -half
                // so here we start from the top
                int worldY = (int) (half - pixelSize * y);

                for (int x = 0; x < canvasPixels; x++) {
                    // x! left = -half, right = half
                    // so here we start on the left
                    int worldX = (int) (-half + pixelSize * x);

                    // a point on the wall, where the ray should hit
                    var targetPosition = Tuple.point(worldX, worldY, wallZ);
                    var ray = new Ray(rayOrigin, targetPosition.sub(rayOrigin).normalize());
                    var intersections = sphere.intersect(ray);
                    var hit = IntersectionTracker.getHit(intersections);
                    if (hit != null) {
                        Tuple position = ray.position(hit.getDistance()); // the point where the ray hit the sphere
                        Tuple normalAtHit = hit.getIntersectedObject().normal(position); // the normal at the point where the ray hit the sphere
                        var eyeVector = ray.direction().normalize().negate();
                        var colorToPaint = sphereMaterial.lighting(lightSource, position, eyeVector, normalAtHit);
                        canvas.write(x, y, colorToPaint);
                    }
                }
            }
        }
        long renderEnd = System.currentTimeMillis();
        Logger.info("Rendertime: {}ms", (renderEnd - renderStart));


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "sphere.ppm", 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
