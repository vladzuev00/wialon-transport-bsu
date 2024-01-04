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
    private static final int MINIMAL_COUNT_TO_BE_SIMPLIFIED = 3;

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
        simplify(coordinates, accumulator, 0, coordinates.size() - 1);
        accumulator.add(coordinates.get(coordinates.size() - 1));
        return accumulator;
    }

    private static void validateEpsilon(final double value) {
        if (!isPositive(value)) {
            throw new IllegalArgumentException("Epsilon should be positive");
        }
    }

    private static boolean isAbleToBeSimplified(final List<Coordinate> coordinates) {
        return coordinates != null && coordinates.size() >= MINIMAL_COUNT_TO_BE_SIMPLIFIED;
    }

    private static List<Coordinate> createSimplifyingAccumulator(final List<Coordinate> coordinates) {
        final List<Coordinate> accumulator = new ArrayList<>();
        accumulator.add(coordinates.get(0));
        return accumulator;
    }

    private void simplify(final List<Coordinate> coordinates,
                          final List<Coordinate> accumulator,
                          final int startIndex,
                          final int endIndex) {
        if (startIndex + 1 == endIndex) {
            return;
        }
        final FurthestCoordinate furthestCoordinate = findFurthestCoordinate(coordinates, startIndex, endIndex);
        final int furthestCoordinateIndex = furthestCoordinate.getIndex();
        if (compare(furthestCoordinate.getDistanceFromLine(), epsilon) > 0) {
            simplify(coordinates, accumulator, startIndex, furthestCoordinateIndex);
            accumulator.add(coordinates.get(furthestCoordinateIndex));
            simplify(coordinates, accumulator, furthestCoordinateIndex, endIndex);
        }
    }

    private FurthestCoordinate findFurthestCoordinate(final List<Coordinate> coordinates,
                                                      final int startIndex,
                                                      final int endIndex) {
        final Line line = new Line(coordinates.get(startIndex), coordinates.get(endIndex));
        return rangeClosed(startIndex, endIndex)
                .mapToObj(i -> new FurthestCoordinate(i, findDistance(line, coordinates.get(i))))
                .max(comparingDouble(FurthestCoordinate::getDistanceFromLine))
                .orElseThrow(() -> new IllegalArgumentException("There is no coordinates"));
    }

    private static double findDistance(final Line line, final Coordinate coordinate) {
        final double triangleDoubleArea = findTriangleDoubleArea(line.first, line.second, coordinate);
        final double lineLength = findLength(line);
        return triangleDoubleArea / lineLength;
    }

    private static double findTriangleDoubleArea(final Coordinate first,
                                                 final Coordinate second,
                                                 final Coordinate third) {
        return abs(
                (second.getLongitude() - first.getLongitude()) * third.getLatitude()
                        - (second.getLatitude() - first.getLatitude()) * third.getLongitude()
                        + second.getLatitude() * first.getLongitude()
                        - second.getLongitude() * first.getLatitude()
        );
    }

    private static double findLength(final Line line) {
        return sqrt(
                square(line.second.getLatitude() - line.first.getLatitude())
                        + square(line.second.getLongitude() - line.first.getLongitude())
        );
    }

    private static double square(final double source) {
        return pow(source, 2);
    }

    @lombok.Value
    private static class Line {
        Coordinate first;
        Coordinate second;
    }

    @lombok.Value
    private static class FurthestCoordinate {
        int index;
        double distanceFromLine;
    }
}
