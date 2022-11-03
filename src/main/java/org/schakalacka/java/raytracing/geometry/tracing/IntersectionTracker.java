package org.schakalacka.java.raytracing.geometry.tracing;

import org.schakalacka.java.raytracing.geometry.objects.GeometryObject;

import java.util.*;

public class IntersectionTracker {

    private static final Map<GeometryObject, List<Intersection>> intersections = new HashMap<>();

    public static void reset() {
        intersections.clear();
    }

    public static void register(Intersection intersection) {
        final var intersectedObject = intersection.getIntersectedObject();
        final List<Intersection> registeredIntersections = intersections.computeIfAbsent(intersectedObject, (o) -> new ArrayList<>());
        registeredIntersections.add(new Intersection(intersectedObject, intersection.getDistance()));
        registeredIntersections.sort(Comparator.comparingDouble(Intersection::getDistance));
    }


    public static List<Intersection> get(GeometryObject object) {
        return Collections.unmodifiableList(intersections.get(object));
    }

    public static Intersection getHit(List<Intersection> intersections) {
        Optional<Intersection> firstPositive = intersections.stream().filter(intersection -> intersection.getDistance() > 0).findFirst();
        return firstPositive.orElse(null);
    }
}
