package org.schakalacka.java.raytracing.renderers;

import org.schakalacka.java.raytracing.*;
import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Canvas;
import org.schakalacka.java.raytracing.scene.Color;
import org.schakalacka.java.raytracing.scene.Environment;
import org.schakalacka.java.raytracing.scene.Projectile;
import org.tinylog.Logger;

public class Chapter2 {

    private Canvas canvas;
    private Environment environment;
    private Projectile projectile;

    public static void main(String[] args) {
        Chapter2 s = new Chapter2();


        Tuple startPosition = Tuple.point(0, 1, 0);
        Tuple startVelocity = Tuple.vector(1,1.8f,0).normalize().mul(11.25f);
        s.projectile = new Projectile(startPosition, startVelocity);

        Tuple gravity = Tuple.vector(0, -0.1f, 0);
        Tuple wind = Tuple.vector(-0.01f, 0, 0);
        s.environment = new Environment(gravity, wind);
        s.canvas = new Canvas(900, 550);

        int steps = 0;
        while (s.projectile.getPosition().y()>=0) {
            s.tick();
            steps++;
            Logger.info("Position: " + s.projectile.getPosition() + " ### Velocity: " + s.projectile.getVelocity());
        }
        Logger.info("Took " + steps + " ticks.");
        long start = System.currentTimeMillis();
        PPMExporter.export(s.canvas, "Chapter2_ballistic.ppm", 255);
        long end = System.currentTimeMillis();
        Logger.info("Export took " + (end - start) + " ms");
    }


    public void tick() {
        canvas.write((int) projectile.getPosition().x(), (int) projectile.getPosition().y(), new Color(0.7,0.7,0.7));
        projectile.setPosition(projectile.getPosition().add(projectile.getVelocity()));
        projectile.setVelocity(projectile.getVelocity().add(environment.gravity()).add(environment.wind()));
    }
}
