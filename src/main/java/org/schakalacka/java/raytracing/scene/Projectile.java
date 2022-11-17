package org.schakalacka.java.raytracing.scene;

import org.schakalacka.java.raytracing.geometry.Tuple;

public class Projectile {

    private Tuple position;
    private Tuple velocity;

    public Projectile(Tuple position, Tuple velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void setPosition(Tuple newPosition) {
        if (newPosition.isPoint()) {
            this.position = newPosition;
        } else {
            throw new RuntimeException("Position has to be a Point");
        }

    }

    public void setVelocity(Tuple newVelocity) {
        if (newVelocity.isVector()) {
            this.velocity = newVelocity;
        } else {
            throw new RuntimeException("Velocity has to be a Vector");
        }

    }

    public Tuple getPosition() {
        return position;
    }

    public Tuple getVelocity() {
        return velocity;
    }

}
