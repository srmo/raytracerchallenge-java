package org.schakalacka.java.raytracing;

public class Starter {

    public static void main(String[] args) {
        Starter s = new Starter();

        Tuple gravity = Tuple.vector(0, -0.1f, 0);
        Tuple wind = Tuple.vector(-0.01f, 0, 0);

        Tuple startPosition = Tuple.point(0, 1, 0);

        // using normalize gives behavior which I do not expect. Probably because I just don't understand it.
        // without normalize() it behaves in a "realistic"/"balistic" way. Of course minus real gravity.
        // Tuple startVelocity = Tuple.vector(10, 10f, 0).normalize();
        Tuple startVelocity = Tuple.vector(10, 10f, 0);

        Environment env = new Environment(gravity, wind);
        Projectile p = new Projectile(startPosition, startVelocity);

        int steps = 0;
        while (p.getPosition().y()>=0f) {
            s.tick(env, p);
            steps++;
            System.out.println("Position: " + p.getPosition() + " ### Velocity: " + p.getVelocity());
        }
        System.out.println("Took " + steps + " ticks.");
    }


    public void tick(Environment env, Projectile projectile) {
        projectile.setPosition(projectile.getPosition().add(projectile.getVelocity()));
        projectile.setVelocity(projectile.getVelocity().add(env.gravity()).add(env.wind()));
    }
}
