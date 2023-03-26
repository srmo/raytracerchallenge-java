package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.math.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

// TODO cleanup this mess. No idea what all those static vars are doing here and unused methods too :/
public class RingPattern extends Pattern {

    private final Color a;
    private final Color b;

    public RingPattern(Color a, Color b) {
        this.a = a;
        this.b = b;
    }

    public static int countA = 0;
    public static int countB = 0;
    public static double minInnerAProduct=1000000;
    public static double maxInnerAProduct=0;

    public static double minInnerBProduct=1000000;
    public static double maxInnerBProduct=0;

    public static double maxX = 0;
    public static double minX = 0;
    public static double maxZ = 0;
    public static double minZ = 0;

    @Override
    public Color patternAt(Tuple point) {

        var innerProduct = Math.pow(point.x(),2) + Math.pow(point.z(),2);

        if (Math.floor(Math.sqrt(innerProduct)) % 2 == 0) {
            countA++;
            minInnerAProduct = Math.min(minInnerAProduct, innerProduct);
            maxInnerAProduct = Math.max(maxInnerAProduct, innerProduct);
            return a;
        } else {
            minInnerBProduct = Math.min(minInnerBProduct, innerProduct);
            maxInnerBProduct = Math.max(maxInnerBProduct, innerProduct);
            countB++;
            return b;
        }
    }

    public Color a() {
        return a;
    }

    public Color b() {
        return b;
    }

}
