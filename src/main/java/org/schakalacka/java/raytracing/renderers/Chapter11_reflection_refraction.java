package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.CheckerPattern;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter11_reflection_refraction {


    public static void main(String[] args) {

        var floor = new Plane();
        CheckerPattern floorPattern = new CheckerPattern(Color.BLACK, Color.WHITE);
        //floorPattern.setTransformationMatrix(MatrixProvider.rotationY((float) Math.toRadians(45)));

        floor.setMaterial(Material.newMaterial().reflectivity(0.2f).color(new Color(1, 0.9, 0.9)).ambient(0.5).pattern(floorPattern).specular(0).create());
//        floor.setMaterial(Material.newMaterial().reflectivity(0.2f).color(new Color(0.2, 0.2, 0.2)).ambient(0.5).specular(0).create());

        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, 1, 0.5f));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                //.diffuse(0.7)
                .specular(0.3)
                        .transparency(0.9f)
                .reflectivity(0.2f)
                .create());

        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floor, middleSphere);

        // 4k resolution
//        var width = 3840;
//        var height = 2160;

        // 1080p resolution
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(0, 1.5f, -5),
                        Tuple.point(0, 1, 0),
                        Tuple.vector(0, 1, 0)
                )
        );

        long renderStart = System.currentTimeMillis();

        var parallelChunks = 8;
        Canvas canvas = camera.render(world, parallelChunks);

        long renderEnd = System.currentTimeMillis();
        long renderTime = renderEnd - renderStart;
        Logger.info("Render-time: {}ms", renderTime);


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter11_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
