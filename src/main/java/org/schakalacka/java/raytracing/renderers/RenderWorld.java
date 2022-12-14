package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.algebra.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.GradientPattern;
import org.schakalacka.java.raytracing.geometry.patterns.Pattern;
import org.schakalacka.java.raytracing.geometry.patterns.StripePattern;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class RenderWorld {


    public static void main(String[] args) {
        Pattern pattern = new GradientPattern(new Color(1, 0.1, 0.1), new Color(1, 1, 0.1));
        pattern.setTransformationMatrix(MatrixProvider.rotationY(Math.PI/4));

        var floor = new Sphere();
        floor.setTransformationMatrix(MatrixProvider.scaling(10, 0.01, 10));
        floor.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).specular(0).create());

        var leftWall = new Sphere();
        leftWall.setTransformationMatrix(
                MatrixProvider.translation(0, 0, 5)
                        .mulM(MatrixProvider.rotationY(-Math.PI / 4))
                        .mulM(MatrixProvider.rotationX(Math.PI / 2))
                        .mulM(MatrixProvider.scaling(10, 0.01, 10))
        );
        leftWall.setMaterial(floor.material());

        var rightWall = new Sphere();
        rightWall.setTransformationMatrix(
                MatrixProvider.translation(0, 0, 5)
                        .mulM(MatrixProvider.rotationY(Math.PI / 4))
                        .mulM(MatrixProvider.rotationX(Math.PI / 2))
                        .mulM(MatrixProvider.scaling(10, 0.01, 10))
        );
        rightWall.setMaterial(floor.material());


        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5, 1, 0.5));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                .diffuse(0.7)
                .specular(0.3)
                .pattern(pattern)
                .create());

        var rightSphere = new Sphere();
        rightSphere.setTransformationMatrix(MatrixProvider.translation(1.5, 0.5, -0.5).mulM(MatrixProvider.scaling(0.5, 0.5, 0.5)));
        rightSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.5, 1, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .pattern(pattern)
                .create());

        var leftSphere = new Sphere();
        leftSphere.setTransformationMatrix(MatrixProvider.translation(-1.5, 0.33, -0.75).mulM(MatrixProvider.scaling(0.33, 0.33, 0.33)));
        leftSphere.setMaterial(Material.newMaterial()
                .color(new Color(1, 0.2, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .pattern(pattern)
                .create());

        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floor, leftWall, rightWall, leftSphere, middleSphere, rightSphere);

        var width = 500;
        var height = 250;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(0, 1.5, -5),
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
        PPMExporter.export(canvas, "chapter7_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
