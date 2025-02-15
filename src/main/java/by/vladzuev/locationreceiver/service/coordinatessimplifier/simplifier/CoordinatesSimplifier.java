package by.vladzuev.locationreceiver.service.coordinatessimplifier.simplifier;

import by.vladzuev.locationreceiver.model.GpsCoordinate;

import java.util.List;

@FunctionalInterface
public interface CoordinatesSimplifier {
    List<GpsCoordinate> simplify(final List<GpsCoordinate> coordinates);
}
