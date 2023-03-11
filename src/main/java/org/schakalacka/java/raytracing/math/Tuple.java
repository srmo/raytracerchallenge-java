package org.schakalacka.java.raytracing.math;

import java.util.Objects;

import static org.schakalacka.java.raytracing.Constants.EQUALS_EPSILON;

// TODO refactor this into an interface and make point and vector first class citizens
public class Tuple {

    private final float[] values = new float[4];

    public Tuple(final float x, final float y, final float z, final float w) {
        values[0] = x;
        values[1] = y;
        values[2] = z;
        values[3] = w;
    }

    public static Tuple tuple(float x, float y, float z, float w) {
        if (w == 0) {
            return new RTVector(x, y, z);
        } else if (w == 1) {
            return new RTPoint(x, y, z);
        } else {
            return new Tuple(x, y, z, w);
        }
    }

    public static RTPoint point(float x, float y, float z) {
        return new RTPoint(x, y, z);
    }

    public static RTVector vector(float x, float y, float z) {
        return new RTVector(x, y, z);
    }

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

    public final boolean isVector() {
        return this instanceof RTVector;
    }

    public final boolean isPoint() {
        return this instanceof RTPoint;
    }

    public Tuple add(Tuple that) {
        return Tuple.tuple(this.x() + that.x(), this.y() + that.y(), this.z() + that.z(), this.w() + that.w());
    }

    public Tuple sub(Tuple that) {
        return Tuple.tuple(this.x() - that.x(), this.y() - that.y(), this.z() - that.z(), this.w() - that.w());
    }

    public Tuple negate() {
        return Tuple.tuple(this.x() * -1, this.y() * -1, this.z() * -1, this.w() * -1);
    }

    public Tuple mul(float scalar) {
        return Tuple.tuple(this.x() * scalar, this.y() * scalar, this.z() * scalar, this.w() * scalar);
    }

    public Tuple div(float scalar) {
        return Tuple.tuple(this.x() / scalar, this.y() / scalar, this.z() / scalar, this.w() / scalar);
    }

    public float magnitude() {
        return (float) Math.sqrt(Math.pow(x(), 2) + Math.pow(y(), 2) + Math.pow(z(), 2) + Math.pow(w(), 2));
    }

    public Tuple normalize() {
        final float magnitude = magnitude();
        return Tuple.tuple(this.x() / magnitude, this.y() / magnitude, this.z() / magnitude, this.w() / magnitude);
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
        return Tuple.vector(
                this.y() * that.z() - this.z() * that.y(),
                this.z() * that.x() - this.x() * that.z(),
                this.x() * that.y() - this.y() * that.x()
        );
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
        if (obj instanceof Tuple that) {
            return Math.abs(this.x() - that.x()) < EQUALS_EPSILON &&
                    Math.abs(this.y() - that.y()) < EQUALS_EPSILON &&
                    Math.abs(this.z() - that.z()) < EQUALS_EPSILON &&
                    Math.abs(this.w() - that.w()) < EQUALS_EPSILON;
        }

        return false;

    }

    public float get(int i) {
        return values[i];
    }

    public Tuple reflect(Tuple that) {
        return this.sub(that.mul(2).mul(this.dot(that)));
    }

    public float[] getArray() {
        return this.values;
    }
}


