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
            return null;
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
