package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.objects.Shape;

import java.util.*;

public class IntersectionTracker {

    private static final Map<Shape, List<Intersection>> intersections = new HashMap<>();

    public static void reset() {
        intersections.clear();
    }

    public static void register(Intersection intersection) {
        final var intersectedObject = intersection.getIntersectedObject();
        final List<Intersection> registeredIntersections = intersections.computeIfAbsent(intersectedObject, (o) -> new ArrayList<>());
        registeredIntersections.add(new Intersection(intersectedObject, intersection.getDistance()));
        registeredIntersections.sort(Comparator.comparingDouble(Intersection::getDistance));
    }


    public static List<Intersection> get(Shape object) {
        return Collections.unmodifiableList(intersections.get(object));
    }

    /***
     *
     * @param intersections a non-null list of Intersections to check for a hit
     * @return exactly one intersection, representing a hit or null, if there was no hit
     */
    public static Intersection getHit(List<Intersection> intersections) {
        Optional<Intersection> firstPositive = intersections.stream().filter(intersection -> intersection.getDistance() > 0).findFirst();

        return firstPositive.orElse(null);
    }
}
