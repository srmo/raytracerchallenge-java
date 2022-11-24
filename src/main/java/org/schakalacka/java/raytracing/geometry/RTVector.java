package org.schakalacka.java.raytracing.geometry;

public class RTVector extends Tuple {

    public RTVector(final double x, final double y, final double z) {
        super(x, y, z, 0);
    }

    public Tuple cross(RTPoint ignoredThat) {
        throw new ArithmeticException("Cross product only defined for Vectors");
    }

    public Tuple sub(RTPoint that) {
        throw new ArithmeticException("Can't subtract Point from Vector");
    }
}
