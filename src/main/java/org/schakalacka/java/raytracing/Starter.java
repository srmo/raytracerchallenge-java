package org.schakalacka.java.raytracing;

public class Starter {

    private Canvas canvas;
    private Environment environment;
    private Projectile projectile;

    public static void main(String[] args) {
        Starter s = new Starter();


        Tuple startPosition = Tuple.point(0, 1, 0);
        Tuple startVelocity = Tuple.vector(1,1.8f,0).normalize().mul(11.25f);
        s.projectile = new Projectile(startPosition, startVelocity);

        Tuple gravity = Tuple.vector(0, -0.1f, 0);
        Tuple wind = Tuple.vector(-0.01f, 0, 0);
        s.environment = new Environment(gravity, wind);
        s.canvas = new Canvas(900, 550);

        int steps = 0;
        while (s.projectile.getPosition().y()>=0f) {
            s.tick();
            steps++;
            System.out.println("Position: " + s.projectile.getPosition() + " ### Velocity: " + s.projectile.getVelocity());
        }
        System.out.println("Took " + steps + " ticks.");
        long start = System.currentTimeMillis();
        PPMExporter.export(s.canvas, "ballistic.ppm", 255);
        long end = System.currentTimeMillis();
        System.out.println("Export took " + (end - start) + " ms");
    }


    public void tick() {
        canvas.write((int) projectile.getPosition().x(), (int) projectile.getPosition().y(), new Color(0.7,0.7,0.7));
        projectile.setPosition(projectile.getPosition().add(projectile.getVelocity()));
        projectile.setVelocity(projectile.getVelocity().add(environment.gravity()).add(environment.wind()));
    }
}
