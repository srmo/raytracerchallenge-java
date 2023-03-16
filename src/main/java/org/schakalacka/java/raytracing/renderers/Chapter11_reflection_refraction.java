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
        floorPattern.setTransformationMatrix(MatrixProvider.scaling(0.4f, 0.4f, 0.4f));

        floor.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).pattern(floorPattern).specular(0).create());

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

        var middleSphere = Sphere.glassySphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, 1.1f, 0.5f));
        middleSphere.setMaterial(Material.newMaterial()
                .refractiveIndex(1.52)
                .transparency(1)
                .reflectivity(0.9f)
                .ambient(0)
                .diffuse(0.4f)
                .specular(0.9f)
                .shininess(300)
                .color(new Color(0.1, 0.2, 0.1))
                .create());

        /*
          color: [0, 0, 0.2]
  ambient: 0
  diffuse: 0.4
  specular: 0.9
  shininess: 300
  reflective: 0.9
  transparency: 0.9
  refractive-index: 1.5
         */

        var innerSphere = Sphere.glassySphere();
        innerSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, 1.2f, 0.5f).mulM(MatrixProvider.scaling(0.3f, 0.3f, 0.3f)));
        innerSphere.setMaterial(Material.newMaterial()
                        .refractiveIndex(1.333)
                        .transparency(0.9f)
                        .reflectivity(0.5f)
                        .ambient(0.3f)
                        .diffuse(0.2f)
                        .specular(0.0f)
                        .shininess(10)
                        .color(new Color(0.1, 0.2, 0.4))
                        .create());


        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floor, middleSphere, innerSphere);

        // 4k resolution
//        var width = 3840;
//        var height = 2160;

        // 1080p resolution
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        // Tuple.point(-0.5f, 10f, 0.5f),
                        Tuple.point(0f, 8f, 0f),
                        Tuple.point(0, 0, 0),
                        Tuple.vector(0, 0, -1)
                )
        );

        long renderStart = System.currentTimeMillis();

        var parallelChunks = 16;
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
