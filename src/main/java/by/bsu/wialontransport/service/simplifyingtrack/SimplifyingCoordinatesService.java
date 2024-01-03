package by.bsu.wialontransport.service.simplifyingtrack;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.simplifyingtrack.simplifier.CoordinatesSimplifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SimplifyingCoordinatesService {
    private final CoordinatesSimplifier trackSimplifier;

    public List<Coordinate> simplify(final List<Coordinate> coordinates) {
        return trackSimplifier.simplify(coordinates);
    }
}
