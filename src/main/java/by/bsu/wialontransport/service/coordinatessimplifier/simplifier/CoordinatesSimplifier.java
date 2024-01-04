package by.bsu.wialontransport.service.coordinatessimplifier.simplifier;

import by.bsu.wialontransport.model.Coordinate;

import java.util.List;

@FunctionalInterface
public interface CoordinatesSimplifier {
    List<Coordinate> simplify(final List<Coordinate> coordinates);
}
