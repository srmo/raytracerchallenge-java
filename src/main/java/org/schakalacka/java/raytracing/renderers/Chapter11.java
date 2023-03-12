package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.Counter;
import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.Pattern;
import org.schakalacka.java.raytracing.geometry.patterns.RingPattern;
import org.schakalacka.java.raytracing.geometry.patterns.TextureMap;
import org.schakalacka.java.raytracing.geometry.patterns.UVCheckerPattern;
import org.schakalacka.java.raytracing.math.MATRIX_TYPE;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UVMapping;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter11 {


    public static void main(String[] args) {
        MatrixProvider.MT = MATRIX_TYPE.CUBLAS;

        Pattern patternRedWhite = new TextureMap(new UVCheckerPattern(10,10, new Color(.6,.2,.2), Color.WHITE), new UVMapping());
        Pattern patternBlackWhite = new TextureMap(new UVCheckerPattern(10,10, Color.BLACK, Color.WHITE), new UVMapping());
        //patternRedWhite.setTransformationMatrix(MatrixProvider.scaling(0.5, 0.5, 0.5));
//        patternRedWhite.setTransformationMatrix(MatrixProvider.scaling(0.2, 0.2, 0.2).mulM(MatrixProvider.rotationZ(Math.toRadians(65))));


        Material floorMaterial = Material.newMaterial().color(new Color(1, 0.9, 0.9)).specular(0).pattern(patternBlackWhite).create();
        Material basicMaterial = Material.newMaterial().color(new Color(1, 0.9, 0.9)).specular(0).create();

        var floor = new Sphere();
        floor.setTransformationMatrix(MatrixProvider.scaling(10, 0.01f, 10));
        floor.setMaterial(floorMaterial);

        var leftWall = new Sphere();
        leftWall.setTransformationMatrix(
                MatrixProvider.translation(0, 0, 5)
                        .mulM(MatrixProvider.rotationY((float) (-Math.PI / 4)))
                        .mulM(MatrixProvider.rotationX((float) (Math.PI / 2)))
                        .mulM(MatrixProvider.scaling(10, 0.01f, 10))
        );
        leftWall.setMaterial(basicMaterial);

        var rightWall = new Sphere();
        rightWall.setTransformationMatrix(
                MatrixProvider.translation(0, 0, 5)
                        .mulM(MatrixProvider.rotationY((float) (Math.PI / 4)))
                        .mulM(MatrixProvider.rotationX((float) (Math.PI / 2)))
                        .mulM(MatrixProvider.scaling(10, 0.01f, 10))
        );
        rightWall.setMaterial(basicMaterial);


        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, 1, 0.5f).mulM(MatrixProvider.rotationY((float) Math.toRadians(-80))));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                .diffuse(0.7)
                .specular(0.3)
                .pattern(patternRedWhite)
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
        world.addObjects(floor, leftWall, rightWall, leftSphere, middleSphere, rightSphere);

        // 4K 2,160 pixels tall and 3,840
        // FHD 1,920 x 1,080 pixels
        var width = 200;
        var height = 200;

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
//        Canvas canvas = camera.renderSinglePixel(world, 300,200);

        long renderEnd = System.currentTimeMillis();
        long renderTime = renderEnd - renderStart;
        Logger.info("Render-time: {}ms", renderTime);


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter7_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
        Logger.info("patternRedWhite call: {} / {}", RingPattern.countA, RingPattern.countB);
        Logger.info("Counter : {} ", new Counter().toString());
    }


}
