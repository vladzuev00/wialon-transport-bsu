package by.vladzuev.locationreceiver.service.mileage.calculator;

import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.service.calculatingdistance.CalculatingDistanceService;
import by.vladzuev.locationreceiver.service.geometrycreating.GeometryCreatingService;
import by.vladzuev.locationreceiver.service.mileage.model.TrackSlice;
import by.vladzuev.locationreceiver.service.coordinatessimplifier.SimplifyingCoordinatesService;
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
