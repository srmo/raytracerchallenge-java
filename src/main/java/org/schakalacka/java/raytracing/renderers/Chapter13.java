package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.patterns.CheckerPattern;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter13 {


    public static void main(String[] args) {

        var floor = new Plane();
        CheckerPattern floorPattern = new CheckerPattern(Color.BLACK, new Color(0.6,0.6,0.6));
        floorPattern.setTransformationMatrix(MatrixProvider.rotationY((float) Math.toRadians(45)));

        floor.setMaterial(Material.newMaterial().reflectivity(0.2f).color(new Color(1, 0.9, 0.9)).ambient(0.5).pattern(floorPattern).specular(0).create());

        var backDrop1 = new Plane();
        CheckerPattern backPattern = new CheckerPattern(Color.BLACK, Color.WHITE);
        backPattern.setTransformationMatrix(MatrixProvider.scaling(0.5f, 0.5f, 0.5f));


        backDrop1.setTransformationMatrix(MatrixProvider.translation(0, 0, 5)
                .mulM(MatrixProvider.rotationY((float) (-Math.PI / 4)))
                .mulM(MatrixProvider.rotationX((float) (Math.PI / 2)))
        );
        backDrop1.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).diffuse(0.8).pattern(backPattern).create());

        var backDrop2 = new Plane();
        backDrop2.setTransformationMatrix(MatrixProvider.translation(0, 0, 5)
                .mulM(MatrixProvider.rotationY((float) (Math.PI / 4)))
                .mulM(MatrixProvider.rotationX((float) (Math.PI / 2)))
        );

        backDrop2.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).diffuse(0.8).pattern(backPattern).create());

        var world = new World(true);
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floor, backDrop1, backDrop2);

        // 4k width height
//        var width = 3840;
//        var height = 2160;

        // full hd
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(3, 5f, -20),
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
        PPMExporter.export(canvas, "chapter13_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
