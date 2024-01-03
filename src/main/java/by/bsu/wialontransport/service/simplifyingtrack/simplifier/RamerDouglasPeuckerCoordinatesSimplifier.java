package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static by.bsu.wialontransport.util.NumberUtil.isPositive;
import static java.lang.Double.compare;
import static java.lang.Math.*;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.IntStream.rangeClosed;

//https://habr.com/ru/articles/448618/
@Component
public final class RamerDouglasPeuckerCoordinatesSimplifier implements CoordinatesSimplifier {
    private static final int MINIMAL_COORDINATE_COUNT_TO_BE_SIMPLIFIED = 3;

    private final double epsilon;

    public RamerDouglasPeuckerCoordinatesSimplifier(@Value("#{new Double('${track-simplifier.ramer-douglas-peucker.epsilon}')}") final double epsilon) {
        validateEpsilon(epsilon);
        this.epsilon = epsilon;
    }

    @Override
    public List<Coordinate> simplify(final List<Coordinate> coordinates) {
        if (!isAbleToBeSimplified(coordinates)) {
            return coordinates;
        }
        final List<Coordinate> accumulator = createSimplifyingAccumulator(coordinates);
        simplifyPart(coordinates, accumulator, 0, coordinates.size() - 1);
        accumulator.add(coordinates.get(coordinates.size() - 1));
        return accumulator;
    }

    private static void validateEpsilon(final double value) {
        if (!isPositive(value)) {
            throw new IllegalArgumentException("Epsilon should be positive");
        }
    }

    private static boolean isAbleToBeSimplified(final List<Coordinate> coordinates) {
        return coordinates != null && coordinates.size() >= MINIMAL_COORDINATE_COUNT_TO_BE_SIMPLIFIED;
    }

    private static List<Coordinate> createSimplifyingAccumulator(final List<Coordinate> coordinates) {
        final List<Coordinate> accumulator = new ArrayList<>();
        accumulator.add(coordinates.get(0));
        return accumulator;
    }

    private void simplifyPart(final List<Coordinate> source,
                              final List<Coordinate> accumulator,
                              final int startPartIndex,
                              final int endPartIndex) {
        if (startPartIndex + 1 == endPartIndex) {
            return;
        }
        final IntAndDoublePair furthestPartPointIndexAndDistanceFromLineOfPartBorders
                = findFurthestPartPointIndexAndDistanceFromLineOfPartBorders(source, startPartIndex, endPartIndex);
        final int furthestPartPointIndex = furthestPartPointIndexAndDistanceFromLineOfPartBorders.getFirst();
        final double furthestPartPointDistanceFromPart = furthestPartPointIndexAndDistanceFromLineOfPartBorders.getSecond();
        if (compare(furthestPartPointDistanceFromPart, epsilon) > 0) {
            simplifyPart(source, accumulator, startPartIndex, furthestPartPointIndex);
            accumulator.add(source.get(furthestPartPointIndex));
            simplifyPart(source, accumulator, furthestPartPointIndex, endPartIndex);
        }
    }

    private IntAndDoublePair findFurthestPartPointIndexAndDistanceFromLineOfPartBorders(final List<Coordinate> source,
                                                                                        final int startPartIndex,
                                                                                        final int endPartIndex) {
        final Coordinate startPartPoint = source.get(startPartIndex);
        final Coordinate endPartPoint = source.get(endPartIndex);
        return rangeClosed(startPartIndex, endPartIndex)
                .mapToObj(i -> new IntAndDoublePair(i, findDistanceBetweenLineAndPoint(startPartPoint, endPartPoint, source.get(i))))
                .max(comparingDouble(IntAndDoublePair::getSecond))
                .orElseThrow(() -> new IllegalArgumentException("Given track part doesn't have points"));
    }

    private static double findDistanceBetweenLineAndPoint(final Coordinate lineFirstPoint,
                                                          final Coordinate lineSecondPoint,
                                                          final Coordinate point) {
        final double doubleAreaOfTriangleOfPoints = findDoubleAreaOfTriangle(lineFirstPoint, lineSecondPoint, point);
        final double lineLength = findDistanceBetweenPoints(lineFirstPoint, lineSecondPoint);
        return doubleAreaOfTriangleOfPoints / lineLength;
    }

    private static double findDoubleAreaOfTriangle(final Coordinate first,
                                                   final Coordinate second,
                                                   final Coordinate third) {
        return abs(
                (second.getLongitude() - first.getLongitude()) * third.getLatitude()
                        - (second.getLatitude() - first.getLatitude()) * third.getLongitude()
                        + second.getLatitude() * first.getLongitude()
                        - second.getLongitude() * first.getLatitude()
        );
    }

    private static double findDistanceBetweenPoints(final Coordinate first, final Coordinate second) {
        return sqrt(
                square(second.getLatitude() - first.getLatitude())
                        + square(second.getLongitude() - first.getLongitude())
        );
    }

    private static double square(final double source) {
        return pow(source, 2);
    }

    @lombok.Value
    private static class IntAndDoublePair {
        int first;
        double second;
    }
}
