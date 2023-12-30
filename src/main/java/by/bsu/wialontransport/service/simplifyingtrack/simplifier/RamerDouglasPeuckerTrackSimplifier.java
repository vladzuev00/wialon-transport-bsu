package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.TempTrack;
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
    private static final int MINIMAL_AMOUNT_OF_POINTS_OF_TRACK_ABLE_TO_BE_SIMPLIFIED = 3;

    private final double epsilon;

    public TempTrack simplify(final TempTrack track) {
        if (!this.isEpsilonPositive() || !isTrackAbleToBeSimplified(track)) {
            return track;
        }
        final List<RequestCoordinate> source = track.getCoordinates();
        final List<RequestCoordinate> simplifiedAccumulator = createSimplifiedAccumulatorBySource(source);
        this.simplifyPart(source, simplifiedAccumulator, 0, source.size() - 1);
        simplifiedAccumulator.add(source.get(source.size() - 1));
        return new TempTrack(simplifiedAccumulator);
    }

    private boolean isEpsilonPositive() {
        return compare(this.epsilon, 0) > 0;
    }

    private static boolean isTrackAbleToBeSimplified(final TempTrack track) {
        final List<RequestCoordinate> coordinates = track.getCoordinates();
        return coordinates != null && coordinates.size() >= MINIMAL_AMOUNT_OF_POINTS_OF_TRACK_ABLE_TO_BE_SIMPLIFIED;
    }

    private static List<RequestCoordinate> createSimplifiedAccumulatorBySource(final List<RequestCoordinate> source) {
        final List<RequestCoordinate> simplifiedAccumulator = new ArrayList<>();
        simplifiedAccumulator.add(source.get(0));
        return simplifiedAccumulator;
    }

    private void simplifyPart(final List<RequestCoordinate> source,
                              final List<RequestCoordinate> simplifiedAccumulator,
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

    private IntAndDoublePair findFurthestPartPointIndexAndDistanceFromLineOfPartBorders(final List<RequestCoordinate> source,
                                                                                        final int startPartIndex,
                                                                                        final int endPartIndex) {
        final RequestCoordinate startPartPoint = source.get(startPartIndex);
        final RequestCoordinate endPartPoint = source.get(endPartIndex);
        return rangeClosed(startPartIndex, endPartIndex)
                .mapToObj(i -> new IntAndDoublePair(i, findDistanceBetweenLineAndPoint(startPartPoint, endPartPoint, source.get(i))))
                .max(comparingDouble(IntAndDoublePair::getSecond))
                .orElseThrow(() -> new IllegalArgumentException("Given track part doesn't have points"));
    }

    private static double findDistanceBetweenLineAndPoint(final RequestCoordinate firstLinePoint,
                                                          final RequestCoordinate secondLinePoint,
                                                          final RequestCoordinate point) {
        final double doubleAreaOfTriangleOfPoints = findDoubleAreaOfTriangle(firstLinePoint, secondLinePoint, point);
        final double lineLength = findDistanceBetweenPoints(firstLinePoint, secondLinePoint);
        return doubleAreaOfTriangleOfPoints / lineLength;
    }

    private static double findDoubleAreaOfTriangle(final RequestCoordinate firstPoint,
                                                   final RequestCoordinate secondPoint,
                                                   final RequestCoordinate thirdPoint) {
        return abs(
                (secondPoint.getLongitude() - firstPoint.getLongitude()) * thirdPoint.getLatitude()
                        - (secondPoint.getLatitude() - firstPoint.getLatitude()) * thirdPoint.getLongitude()
                        + secondPoint.getLatitude() * firstPoint.getLongitude()
                        - secondPoint.getLongitude() * firstPoint.getLatitude()
        );
    }

    private static double findDistanceBetweenPoints(final RequestCoordinate first, final RequestCoordinate second) {
        return sqrt(
                square(second.getLatitude() - first.getLatitude())
                        + square(second.getLongitude() - first.getLongitude())
        );
    }

    private static double square(final double source) {
        return pow(source, 2);
    }

    @Value
    private static class IntAndDoublePair {
        int first;
        double second;
    }
}
