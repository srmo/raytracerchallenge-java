package org.schakalacka.java.raytracing.geometry.objects;

import org.schakalacka.java.raytracing.geometry.tracing.Intersection;
import org.schakalacka.java.raytracing.geometry.tracing.Ray;
import org.schakalacka.java.raytracing.math.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Group extends Shape {
    private final List<Shape> children = new ArrayList<>();

    @Override
    public Bounds getBounds() {
        if (children.size() == 0) {
            return new Bounds(Tuple.point(0, 0, 0), Tuple.point(0, 0, 0));
        } else {
            var initBounds = new Bounds(Tuple.point(0, 0, 0), Tuple.point(0, 0, 0));
            for (Shape child:children) {
                var lowerBounds = child.getBounds().getLower();
                var upperBounds = child.getBounds().getUpper();
                var currentLower = initBounds.getLower();
                var currentUpper = initBounds.getUpper();

                var minLowerX = Math.min(lowerBounds.x(), currentLower.x());
                var minLowerY = Math.min(lowerBounds.y(), currentLower.y());
                var minLowerZ = Math.min(lowerBounds.z(), currentLower.z());
                var maxUpperX = Math.max(upperBounds.x(), currentUpper.x());
                var maxUpperY = Math.max(upperBounds.y(), currentUpper.y());
                var maxUpperZ = Math.max(upperBounds.z(), currentUpper.z());

                initBounds = new Bounds(Tuple.point(minLowerX, minLowerY, minLowerZ),
                        Tuple.point(maxUpperX, maxUpperY, maxUpperZ));

            }
            return initBounds;
        }
    }

    @Override
    public List<Intersection> localIntersect(Ray ray) {
        var intersections = new ArrayList<Intersection>();
        for (Shape child:children) {
            intersections.addAll(child.intersect(ray));
        }
        intersections.sort(Comparator.comparingDouble(Intersection::getDistance));
        return intersections;
    }

    @Override
    public Tuple localNormalVectorAt(Tuple point) {
        throw new UnsupportedOperationException("Group does not have a normal vector");
    }

    public List<Shape> getChildren() {
        return children;
    }

    public Group addChild(Shape s) {
        children.add(s);
        s.setParent(this);
        return this;
    }
}
