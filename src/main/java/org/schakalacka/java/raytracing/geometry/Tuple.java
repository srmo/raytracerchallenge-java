package org.schakalacka.java.raytracing.geometry;

import java.util.Objects;

public class Tuple {

    public static final double EPSILON = 0.0000001;

    private final float[] values = new float[4];
    public float x() {
        return values[0];
    }

    public float y() {
        return values[1];
    }

    public float z() {
        return values[2];
    }

    public float w() {
        return values[3];
    }

    private Tuple(final float x, final float y, final float z, final float w) {
        values[0] = x;
        values[1] = y;
        values[2] = z;
        values[3] = w;
    }

    public static Tuple tuple(float x, float y, float z, float w) {
        return new Tuple(x, y, z, w);
    }

    public static Tuple point(float x, float y, float z) {
        return new Tuple(x, y, z, 1.0f);
    }

    public static Tuple vector(float x, float y, float z) {
        return new Tuple(x, y, z, 0.0f);
    }

    public final boolean isVector() {
        return w() == 0.0f;
    }

    public final boolean isPoint() {
        return w() == 1.0f;
    }

    public Tuple add(Tuple that) {
        if (this.isPoint() && that.isPoint()) {
            throw new ArithmeticException("Can't add two Points");
        }
        return new Tuple(this.x() + that.x(), this.y() + that.y(), this.z() + that.z(), this.w() + that.w());

    }

    public Tuple sub(Tuple that) {
        if (this.isVector() && that.isPoint()) {
            throw new ArithmeticException("Can't subtract Point from Vector");
        }
        return new Tuple(this.x() - that.x(), this.y() - that.y(), this.z() - that.z(), this.w() - that.w());
    }

    public Tuple negate() {
        return new Tuple(this.x() * -1f, this.y() * -1f, this.z() * -1f, this.w() * -1f);
    }

    public Tuple mul(float scalar) {
        return new Tuple(this.x() * scalar, this.y() * scalar, this.z() * scalar, this.w() * scalar);
    }

    public Tuple div(float scalar) {
        return new Tuple(this.x() / scalar, this.y() / scalar, this.z() / scalar, this.w() / scalar);
    }

    public double magnitude() {
        return Math.sqrt(x() * x() + y() * y() + z() * z());
    }

    public Tuple normalize() {
        final float magnitude = (float) magnitude();
        return new Tuple(this.x() / magnitude, this.y() / magnitude, this.z() / magnitude, this.w() / magnitude);
    }

    public float dot(Tuple that) {
        return this.x() * that.x()
                + this.y() * that.y()
                + this.z() * that.z()
                + this.w() * that.w();
    }


    /***
     * We define this only for our 3 dimensional vectors
     * @param that another vector. Not null
     * @return the new Vector as result of the cross product. Never null
     * @throws ArithmeticException when either this or that tuple isn't a vector
     */
    public Tuple cross(Tuple that) {
        if (this.isVector() && that.isVector()) {
            return Tuple.vector(
                    this.y() * that.z() - this.z() * that.y(),
                    this.z() * that.x() - this.x() * that.z(),
                    this.x() * that.y() - this.y() * that.x()
            );
        } else {
            throw new ArithmeticException("Cross product only defined for Vectors");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x() +
                ", y=" + y() +
                ", z=" + z() +
                ", w=" + w() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y(), z(), w());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Tuple) {
            return
                    this.x() - ((Tuple) obj).x() < EPSILON &&
                            this.y() - ((Tuple) obj).y() < EPSILON &&
                            this.z() - ((Tuple) obj).z() < EPSILON &&
                            this.w() - ((Tuple) obj).w() < EPSILON;
        }

        return false;

    }

    public float get(int i) {
        return values[i];
    }
}


