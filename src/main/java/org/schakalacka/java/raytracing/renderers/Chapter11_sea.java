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

public class Chapter11_sea {


    public static void main(String[] args) {
        var seafloor = new Plane();
        var seafloorPattern = new CheckerPattern(Color.BLACK, Color.WHITE);
        seafloorPattern.setTransformationMatrix(MatrixProvider.scaling(0.4f, 0.4f, 0.4f));
        seafloor.setTransformationMatrix(MatrixProvider.translation(0, -8, 0));
        seafloor.setMaterial(Material.newMaterial().color(new Color(1, 0.9, 0.9)).ambient(0.5).pattern(seafloorPattern).specular(0).create());

        var waterlevel = new Plane();

        waterlevel.setMaterial(Material.newMaterial().color(new Color(0, 0.1, 0.2))
                .ambient(0.2)
                .diffuse(0.7)
                .specular(0.9)
                .shininess(300)
                .reflectivity(0.2f)
                .transparency(0.9)
                .refractiveIndex(1.33)
                .createsShadow(false)
                .create());
        waterlevel.setTransformationMatrix(MatrixProvider.translation(0, 1, 0));

        var middleSphere = Sphere.glassySphere();
        middleSphere.setTransformationMatrix(MatrixProvider.translation(-0.5f, -2.1f, 0.5f));
        middleSphere.setMaterial(Material.newMaterial()
                .ambient(0.4)
                .diffuse(0.4f)
                .specular(0.9f)
                .shininess(10)
                .color(new Color(.6, 0.2, 0.3))
                .create());

        var otherSphere = Sphere.glassySphere();
        otherSphere.setTransformationMatrix(MatrixProvider.translation(-2.5f, -2f, 0.5f).mulM(MatrixProvider.scaling(0.5f, 0.5f, 0.5f)));
        otherSphere.setMaterial(Material.newMaterial()
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
        world.addObjects(seafloor, waterlevel, middleSphere, otherSphere);

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
                        Tuple.point(0f, 2f, 2f),
                        Tuple.point(0, 0, 1),
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
        PPMExporter.export(canvas, "chapter11_sea_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
