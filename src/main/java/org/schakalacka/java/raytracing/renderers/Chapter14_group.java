package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.PPMExporter;
import org.schakalacka.java.raytracing.geometry.objects.Group;
import org.schakalacka.java.raytracing.geometry.objects.Sphere;
import org.schakalacka.java.raytracing.geometry.patterns.TextureMap;
import org.schakalacka.java.raytracing.geometry.patterns.UVCheckerPattern;
import org.schakalacka.java.raytracing.math.MatrixProvider;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.math.UVMapping;
import org.schakalacka.java.raytracing.scene.*;
import org.schakalacka.java.raytracing.world.ViewTransformation;
import org.schakalacka.java.raytracing.world.World;
import org.tinylog.Logger;

public class Chapter14_group {


    public static void main(String[] args) {
        var pattern = new TextureMap(new UVCheckerPattern(4,4, new Color(1,0,0), new Color(0,0,1)), new UVMapping());
        pattern.setTransformationMatrix(MatrixProvider.rotationX(Math.toRadians(45)));
        var g1 = new Group();
        g1.setTransformationMatrix(MatrixProvider.rotationY(Math.PI / 2));

        var g2 = new Group();
        g2.setTransformationMatrix(MatrixProvider.scaling(1, 2, 3));

        g1.addChild(g2);

        var s = new Sphere();
        s.setTransformationMatrix(MatrixProvider.translation(5, 0, 0));
        s.setMaterial(Material.newMaterial().pattern(pattern).create());

        g2.addChild(s);


        var world = new World(true);
        world.setLightSource(new PointLight(Tuple.point(-10, 20, -10), new Color(0.8, 0.8, 0.8)));
        world.addObjects(g1);

        // 4k width height
//        var width = 3840;
//        var height = 2160;

        // full hd
        var width = 1920;
        var height = 1080;

        var camera = new Camera(width, height, Math.PI / 3);
        camera.setTransformationMatrix(ViewTransformation
                .transform(
                        Tuple.point(13, 5f, -20),
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
        PPMExporter.export(canvas, "chapter14_%dx%d_chunks_%d_Matrix_%s.ppm".formatted(width, height, parallelChunks, MatrixProvider.MT), 255);
        long exportEnd = System.currentTimeMillis();
        Logger.info("Export took {}ms", (exportEnd - exportStart));
    }


}
