package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.algebra.MatrixProvider;
import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.geometry.objects.Plane;
import org.schakalacka.java.raytracing.geometry.objects.Shape;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class RenderWorldHexagonPlanes {


    public static void main(String[] args) {
        var floor1 = new Plane();
        floor1.setMaterial(Material.newMaterial().color(new Color(1, 0.5, 0.5)).specular(0).create());

        var floor2 = new Plane();
        Material blackMaterial = Material.newMaterial().color(new Color(0.1,0.5,0.5)).shininess(500).specular(0).create();
        floor2.setMaterial(blackMaterial);
        floor2.setTransformationMatrix(MatrixProvider.translation(0, 0, 5)
                .mulM(MatrixProvider.rotationX(Math.PI / 2)));
                //.mulM(MatrixProvider.rotationX(Math.PI / 2)));

        var floor3 = new Plane();
        floor3.setMaterial(floor2.material());
        floor3.setTransformationMatrix(MatrixProvider.translation(-10,0,0).mulM(MatrixProvider.rotationZ(-Math.PI/4)));

        var floor4 = new Plane();
        floor4.setMaterial(floor2.material());
        floor4.setTransformationMatrix(MatrixProvider.translation(-10,0,0).mulM(MatrixProvider.rotationY(-Math.PI/2)).mulM(MatrixProvider.rotationZ(-Math.PI/4)));

        var floor5 = new Plane();
        floor5.setMaterial(floor2.material());
        floor5.setTransformationMatrix(MatrixProvider.translation(10,0,0).mulM(MatrixProvider.rotationY(-Math.PI/4)).mulM(MatrixProvider.rotationZ(Math.PI/4)));

        List<Shape> floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);
        //floors.add(floor3);
        //floors.add(floor4);

        var middleSphere = new Sphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5, -0.5, 0.5));
        middleSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.1, 1, 0.5))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var rightSphere = new Sphere();
        rightSphere.setTransformationMatrix(MatrixProvider.translation(1.5, 0.5, -0.5).mulM(MatrixProvider.scaling(0.5, 0.5, 0.5)));
        rightSphere.setMaterial(Material.newMaterial()
                .color(new Color(0.5, 1, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var leftSphere = new Sphere();
        leftSphere.setTransformationMatrix(MatrixProvider.translation(-1.5, 0.33, -0.75).mulM(MatrixProvider.scaling(0.33, 0.33, 0.33)));
        leftSphere.setMaterial(Material.newMaterial()
                .color(new Color(1, 0.2, 0.1))
                .diffuse(0.7)
                .specular(0.3)
                .create());

        var world = new World();
        world.setLightSource(new PointLight(Tuple.point(0, 10, 0), new Color(0.8, 0.8, 0.8)));
        world.addObjects(floors.toArray(new Shape[0]));
        world.addObjects(leftSphere, middleSphere, rightSphere);

        var width = 500;
        var height = 500;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(0, 0.5, -5),
                        Tuple.point(0, 0.5, 10),
                        Tuple.vector(0, 1, 0)
//                        Tuple.point(0, 10, 0),
//                        Tuple.point(0, 0, 0),
//                        Tuple.vector(0, 0, 1)
                )
        );

        long renderStart = System.currentTimeMillis();

        var parallelChunks = 8;
        Canvas canvas = camera.render(world, parallelChunks);

        long renderEnd = System.currentTimeMillis();
        long renderTime = renderEnd - renderStart;
        Logger.info("Render-time: {}ms", renderTime);


        long exportStart = System.currentTimeMillis();
        PPMExporter.export(canvas, "chapter9_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
