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
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public final class RamerDouglasPeuckerTrackSimplifier implements TrackSimplifier {
    private static final int SQUARE_EXPONENT = 2;

    private final double epsilon;

    public Track simplify(final Track track) {
        final List<Coordinate> source = track.getCoordinates();
        final List<Coordinate> simplifiedAccumulator = new ArrayList<>();
        this.simplifyPart(source, simplifiedAccumulator, 0, source.size() - 1);
        return new Track(simplifiedAccumulator);
    }

    private void simplifyPart(final List<Coordinate> source,
                              final List<Coordinate> simplifiedAccumulator,
                              final int startPartIndex,
                              final int endPartIndex) {
        if (startPartIndex + 1 == endPartIndex) {
            return;
        }
        final PairIntAndDouble furthestPointIndexAndDistanceFromLineOfPartBorders
                = findFurthestPointIndexAndDistanceFromLineOfPartBorders(source, startPartIndex, endPartIndex);
        final int furthestPointIndex = furthestPointIndexAndDistanceFromLineOfPartBorders.getFirst();
        final double furthestPointDistanceFromPart = furthestPointIndexAndDistanceFromLineOfPartBorders.getSecond();
        if (compare(furthestPointDistanceFromPart, this.epsilon) > 0) {
            this.simplifyPart(source, simplifiedAccumulator, startPartIndex, furthestPointIndex);
            simplifiedAccumulator.add(source.get(furthestPointIndex));
            this.simplifyPart(source, simplifiedAccumulator, furthestPointIndex, endPartIndex);
        }
    }

    private PairIntAndDouble findFurthestPointIndexAndDistanceFromLineOfPartBorders(final List<Coordinate> source,
                                                                                    final int startPartIndex,
                                                                                    final int endPartIndex) {
        final Coordinate startPartPoint = source.get(startPartIndex);
        final Coordinate endPartPoint = source.get(endPartIndex);
        return range(0, source.size())
                .mapToObj(i -> new PairIntAndDouble(i, findDistanceBetweenLineAndPoint(startPartPoint, endPartPoint, source.get(i))))
                .max(comparingDouble(PairIntAndDouble::getSecond))
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
    private static class PairIntAndDouble {
        int first;
        double second;
    }
}
