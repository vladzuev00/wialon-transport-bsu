package by.bsu.wialontransport.service.coordinatessimplifier.simplifier;

import by.bsu.wialontransport.model.GpsCoordinate;

import java.util.List;

@FunctionalInterface
public interface CoordinatesSimplifier {
    List<GpsCoordinate> simplify(final List<GpsCoordinate> coordinates);
}
