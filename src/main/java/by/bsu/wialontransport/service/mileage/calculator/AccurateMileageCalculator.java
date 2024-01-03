package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingCoordinatesService;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public final class AccurateMileageCalculator extends MileageCalculator {

    public AccurateMileageCalculator(final SimplifyingCoordinatesService simplifyingCoordinatesService,
                                     final GeometryCreatingService geometryCreatingService,
                                     final CalculatingDistanceService calculatingDistanceService,
                                     final AddressService addressService) {
        super(simplifyingCoordinatesService, geometryCreatingService, calculatingDistanceService, addressService);
    }

    @Override
    protected Stream<TrackSlice> createTrackSlices(final TrackSlicesCreatingContext context) {
        throw new UnsupportedOperationException();
    }
}
