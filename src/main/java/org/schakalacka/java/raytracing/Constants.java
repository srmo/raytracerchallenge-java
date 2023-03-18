package org.schakalacka.java.raytracing;


// yay, anti-pattern. don't have a better name right now :)
public final class Constants {

    //    public static final double EPSILON = 0.000000000001;
    public static final float EQUALS_EPSILON = 0.0001f;
    public static final float SHAPE_POINT_OFFSET_EPSILON = 0.0000001f;

    public static int DEFAULT_REFLECTION_DEPTH = 5;

    private Constants(){}
}
