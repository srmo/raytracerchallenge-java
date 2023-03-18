package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.CheckerPattern;
import org.schakalacka.java.raytracing.geometry.patterns.Pattern;
import org.schakalacka.java.raytracing.geometry.patterns.TextureMap;
import org.schakalacka.java.raytracing.geometry.patterns.UVCheckerPattern;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UVMapping;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter11 {


    public static void main(String[] args) {
        Pattern patternRedWhite = new TextureMap(new UVCheckerPattern(20, 10, new Color(1, .2, .2), Color.WHITE), new UVMapping());


        var floor = new Plane();
        CheckerPattern floorPattern = new CheckerPattern(Color.BLACK, Color.WHITE);
        floorPattern.setTransformationMatrix(MatrixProvider.rotationY( Math.toRadians(45)));

        floor.setMaterial(Material.newMaterial().reflectivity(0.2f).color(new Color(1, 0.9, 0.9)).ambient(0.5).pattern(floorPattern).specular(0).create());

        var backDrop1 = new Plane();
        CheckerPattern backPattern = new CheckerPattern(Color.BLACK, Color.WHITE);
        backPattern.setTransformationMatrix(MatrixProvider.scaling(0.5f, 0.5f, 0.5f));


        backDrop1.setTransformationMatrix(MatrixProvider.translation(0, 0, 5)
                .mulM(MatrixProvider.rotationY( (-Math.PI / 4)))
                .mulM(MatrixProvider.rotationX( (Math.PI / 2)))
        );
        backDrop1.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).diffuse(0.8).pattern(backPattern).create());

        var backDrop2 = new Plane();
        backDrop2.setTransformationMatrix(MatrixProvider.translation(0, 0, 5)
                .mulM(MatrixProvider.rotationY( (Math.PI / 4)))
                .mulM(MatrixProvider.rotationX( (Math.PI / 2)))
        );

        backDrop2.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).diffuse(0.8).pattern(backPattern).create());

        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, 1, 0.5f));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                //.diffuse(0.7)
                .specular(0.3)
                .pattern(patternRedWhite)
                .reflectivity(0.8f)
                .create());

        var rightSphere = new Sphere();
        rightSphere.setTransformationMatrix(MatrixProvider.translation(1.5f, 0.5f, -0.5f).mulM(MatrixProvider.scaling(0.5f, 0.5f, 0.5f)));
        rightSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.5, 1, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var leftSphere = new Sphere();
        leftSphere.setTransformationMatrix(MatrixProvider.translation(-1.5f, 0.33f, -0.75f).mulM(MatrixProvider.scaling(0.33f, 0.33f, 0.33f)));
        leftSphere.setMaterial(Material.newMaterial()
                .color(new Color(1, 0.2, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floor, backDrop1, backDrop2, leftSphere, middleSphere, rightSphere);

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
