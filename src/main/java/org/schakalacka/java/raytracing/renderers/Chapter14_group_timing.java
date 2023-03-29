package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Group;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter14_group_timing {


    public static void main(String[] args) {
        Material sphereMaterial = Material.newMaterial().transparency(0.5).reflectivity(0.4).refractiveIndex(1.2).create();
        var g0 = new Group();

        var g1 = new Group();

        var g2 = new Group();

        var s0 = new Sphere();
        s0.setTransformationMatrix(MatrixProvider.translation(0, .5, 0));
        s0.setMaterial(sphereMaterial);

        var s1 = new Sphere();
        s1.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));
        s1.setMaterial(sphereMaterial);
        var s2 = new Sphere();
        s2.setTransformationMatrix(MatrixProvider.translation(2, 0, 0));
        s2.setMaterial(sphereMaterial);
        var s3 = new Sphere();
        s3.setTransformationMatrix(MatrixProvider.translation(-5, 0, 0));
        s3.setMaterial(sphereMaterial);
        var s4 = new Sphere();
        s4.setTransformationMatrix(MatrixProvider.translation(-2, 0, 0));
        s4.setMaterial(sphereMaterial);

        g0.addChild(s0);
        g1.addChild(s1);
        g1.addChild(s2);
        g2.addChild(s3);
        g2.addChild(s4);


        var world = new World(true);
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(g0,g1,g2);

        // 4k width height
//        var width = 3840;
//        var height = 2160;

        // full hd
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(0, 5f, -10),
                        Tuple.point(0, 1, 0),
                        Tuple.vector(0, 1, 0)
                )
        );

        long renderStart = System.currentTimeMillis();

        var parallelChunks = 16;
        Canvas canvas = camera.render(world, parallelChunks);

        long renderEnd = System.currentTimeMillis();
        long renderTime = renderEnd - renderStart;
        Logger.info("Render-time: {}ms", renderTime);


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter14_group_timing%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
        Logger.info("Total rays seen in groups:  g1={}; g2={} - total rays ignored: g1={}; g2={}", g1.totalRaysSeen, g2.totalRaysSeen, g1.groupMissCount, g2.groupMissCount);
    }


}
