package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.Matrix;
import org.schakalacka.java.raytracing.geometry.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class RenderWorld {


    public static void main(String[] args) {
        var floor = new Sphere();
        floor.setTransformationMatrix(Matrix.scaling(10, 0.01, 10));
        floor.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).specular(0).create());

        var leftWall = new Sphere();
        leftWall.setTransformationMatrix(
                Matrix.translation(0, 0, 5)
                        .mulM(Matrix.rotationY(-Math.PI / 4))
                        .mulM(Matrix.rotationX(Math.PI / 2))
                        .mulM(Matrix.scaling(10, 0.01, 10))
        );
        leftWall.setMaterial(floor.material());

        var rightWall = new Sphere();
        rightWall.setTransformationMatrix(
                Matrix.translation(0, 0, 5)
                        .mulM(Matrix.rotationY(Math.PI / 4))
                        .mulM(Matrix.rotationX(Math.PI / 2))
                        .mulM(Matrix.scaling(10, 0.01, 10))
        );
        rightWall.setMaterial(floor.material());


        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(Matrix.translation(-0.5, 1, 0.5));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var rightSphere = new Sphere();
        rightSphere.setTransformationMatrix(Matrix.translation(1.5, 0.5, -0.5).mulM(Matrix.scaling(0.5, 0.5, 0.5)));
        rightSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.5, 1, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var leftSphere = new Sphere();
        leftSphere.setTransformationMatrix(Matrix.translation(-1.5, 0.33, -0.75).mulM(Matrix.scaling(0.33, 0.33, 0.33)));
        leftSphere.setMaterial(Material.newMaterial()
                .color(new Color(1, 0.2, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(-10, 10, -10), new Color(1, 1, 1)));
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

        var parallelChunks = 16;
        Canvas canvas = camera.render(world, parallelChunks);

        long renderEnd = System.currentTimeMillis();
        long renderTime = renderEnd - renderStart;
        Logger.info("Render-time: {}ms", renderTime);


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter7_%dx%d_chunks_%d.ppm".formatted(width, height, parallelChunks), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
