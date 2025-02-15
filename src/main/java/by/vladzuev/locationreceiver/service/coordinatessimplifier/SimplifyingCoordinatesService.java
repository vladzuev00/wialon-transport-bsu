package by.vladzuev.locationreceiver.service.coordinatessimplifier;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.coordinatessimplifier.simplifier.CoordinatesSimplifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SimplifyingCoordinatesService {
    private final CoordinatesSimplifier trackSimplifier;

    public List<GpsCoordinate> simplify(final List<GpsCoordinate> coordinates) {
        return trackSimplifier.simplify(coordinates);
    }
}
