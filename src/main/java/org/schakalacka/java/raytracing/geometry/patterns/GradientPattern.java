package org.schakalacka.java.raytracing.geometry.patterns;

import org.schakalacka.java.raytracing.geometry.algebra.Tuple;
import org.schakalacka.java.raytracing.scene.Color;

// TODO go wild with gradients! For example, how to wrap-around a gradient? https://stackoverflow.com/questions/22607043/color-gradient-algorithm
public class GradientPattern extends Pattern {

    private final Color a;
    private final Color b;

    public GradientPattern(Color a, Color b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Color patternAt(Tuple point) {
        var distance = b.sub(a);
        var fraction = point.x() - Math.floor(point.x()); // just the fraction of X...why?

        return a.add(distance.mulS(fraction));

    }

}
