package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.coordinatessimplifier.SimplifyingCoordinatesService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "mileage-calculator", name = "enable-accurate-calculator", havingValue = "true")
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
