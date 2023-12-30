package by.bsu.wialontransport.service.calculatingdistance;

import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.TempTrack;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.*;
import static java.util.stream.IntStream.range;

@Service
public final class CalculatingDistanceService {
    private static final double EARTH_RADIUS_IN_KILOMETERS = 6378.136;

    public double calculate(final TempTrack track) {
        final List<RequestCoordinate> coordinates = track.getCoordinates();
        return range(0, coordinates.size() - 1)
                .mapToDouble(i -> calculate(coordinates.get(i), coordinates.get(i + 1)))
                .sum();
    }

    public double calculate(final RequestCoordinate first, final RequestCoordinate second) {
        final double latitudeDistance = toRadians(second.getLatitude() - first.getLatitude());
        final double longitudeDistance = toRadians(second.getLongitude() - first.getLongitude());
        final double a = square(sin(latitudeDistance / 2))
                + cos(toRadians(first.getLatitude()))
                * cos(toRadians(second.getLatitude()))
                * square(sin(longitudeDistance / 2));
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return EARTH_RADIUS_IN_KILOMETERS * c;
    }

    private static double square(final double value) {
        return pow(value, 2);
    }
}
