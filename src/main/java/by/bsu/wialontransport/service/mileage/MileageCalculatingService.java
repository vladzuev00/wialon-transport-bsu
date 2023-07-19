package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class MileageCalculatingService {
    private final SimplifyingTrackService simplifyingTrackService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final CityService cityService;

    @Value
    private static class TrackSlice {
        Coordinate first;
        Coordinate second;
        boolean locatedInCity;
    }
}
