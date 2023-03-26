package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Cylinder;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter13_infinite_cylinder {


    public static void main(String[] args) {

        Material cylinderMaterial = Material.newMaterial().color(new Color(0.2, 0.01, 0.2)).ambient(1).diffuse(1).specular(1).shininess(300).reflectivity(0).transparency(0.7).refractiveIndex(1).create();
        var cylinder = new Cylinder();
        cylinder.setClosed(true);
        cylinder.setMaterial(cylinderMaterial);
        cylinder.setTransformationMatrix(MatrixProvider.rotationX( Math.toRadians(45)));

        var world = new World(true);
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(cylinder);

        // 4k width height
//        var width = 3840;
//        var height = 2160;

        // full hd
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(3, 5f, -15),
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
        PPMExporter.export(canvas, "chapter13_infiniteCyl_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
