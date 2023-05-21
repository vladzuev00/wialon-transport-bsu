package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.compare;
import static java.lang.Math.*;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.IntStream.rangeClosed;

//https://habr.com/ru/articles/448618/
@RequiredArgsConstructor
public final class RamerDouglasPeuckerTrackSimplifier implements TrackSimplifier {
    private static final int SQUARE_EXPONENT = 2;
    private static final int MINIMAL_AMOUNT_OF_POINTS_OF_TRACK_ABLE_TO_BE_SIMPLIFIED = 3;

    private final double epsilon;

    public Track simplify(final Track track) {
        if (!this.isEpsilonPositive() || !isTrackAbleToBeSimplified(track)) {
            return track;
        }
        final List<Coordinate> source = track.getCoordinates();
        final List<Coordinate> simplifiedAccumulator = createSimplifiedAccumulatorBySource(source);
        this.simplifyPart(source, simplifiedAccumulator, 0, source.size() - 1);
        simplifiedAccumulator.add(source.get(source.size() - 1));
        return new Track(simplifiedAccumulator);
    }

    private boolean isEpsilonPositive() {
        return compare(this.epsilon, 0) > 0;
    }

    private static boolean isTrackAbleToBeSimplified(final Track track) {
        final List<Coordinate> coordinates = track.getCoordinates();
        return coordinates != null && coordinates.size() >= MINIMAL_AMOUNT_OF_POINTS_OF_TRACK_ABLE_TO_BE_SIMPLIFIED;
    }

    private static List<Coordinate> createSimplifiedAccumulatorBySource(final List<Coordinate> source) {
        final List<Coordinate> simplifiedAccumulator = new ArrayList<>();
        simplifiedAccumulator.add(source.get(0));
        return simplifiedAccumulator;
    }

    private void simplifyPart(final List<Coordinate> source,
                              final List<Coordinate> simplifiedAccumulator,
                              final int startPartIndex,
                              final int endPartIndex) {
        if (startPartIndex + 1 == endPartIndex) {
            return;
        }
        final IntAndDoublePair furthestPartPointIndexAndDistanceFromLineOfPartBorders
                = findFurthestPartPointIndexAndDistanceFromLineOfPartBorders(source, startPartIndex, endPartIndex);
        final int furthestPartPointIndex = furthestPartPointIndexAndDistanceFromLineOfPartBorders.getFirst();
        final double furthestPartPointDistanceFromPart = furthestPartPointIndexAndDistanceFromLineOfPartBorders.getSecond();
        if (compare(furthestPartPointDistanceFromPart, this.epsilon) > 0) {
            this.simplifyPart(source, simplifiedAccumulator, startPartIndex, furthestPartPointIndex);
            simplifiedAccumulator.add(source.get(furthestPartPointIndex));
            this.simplifyPart(source, simplifiedAccumulator, furthestPartPointIndex, endPartIndex);
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

    private static double findDistanceBetweenLineAndPoint(final Coordinate firstLinePoint,
                                                          final Coordinate secondLinePoint,
                                                          final Coordinate point) {
        final double doubleAreaOfTriangleOfPoints = findDoubleAreaOfTriangle(firstLinePoint, secondLinePoint, point);
        final double lineLength = findDistanceBetweenPoints(firstLinePoint, secondLinePoint);
        return doubleAreaOfTriangleOfPoints / lineLength;
    }

    private static double findDoubleAreaOfTriangle(final Coordinate firstPoint,
                                                   final Coordinate secondPoint,
                                                   final Coordinate thirdPoint) {
        return abs(
                (secondPoint.getLongitude() - firstPoint.getLongitude()) * thirdPoint.getLatitude()
                        - (secondPoint.getLatitude() - firstPoint.getLatitude()) * thirdPoint.getLongitude()
                        + secondPoint.getLatitude() * firstPoint.getLongitude()
                        - secondPoint.getLongitude() * firstPoint.getLatitude()
        );
    }

    private static double findDistanceBetweenPoints(final Coordinate first, final Coordinate second) {
        return sqrt(
                findSquare(second.getLatitude() - first.getLatitude())
                        + findSquare(second.getLongitude() - first.getLongitude())
        );
    }

    private static double findSquare(final double source) {
        return pow(source, SQUARE_EXPONENT);
    }

    @Value
    private static class IntAndDoublePair {
        int first;
        double second;
    }
}
