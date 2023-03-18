package org.schakalacka.java.raytracing.math;

public class RTVector extends Tuple {

    public RTVector(final double x, final double y, final double z) {
        super(x, y, z, 0);
    }
    @SuppressWarnings("UnusedReturnValue")
    public Tuple cross(RTPoint ignoredThat) {
        throw new ArithmeticException("Cross product only defined for Vectors");
    }

    public Tuple sub(RTPoint ignoredThat) {
        throw new ArithmeticException("Can't subtract Point from Vector");
    }
}
