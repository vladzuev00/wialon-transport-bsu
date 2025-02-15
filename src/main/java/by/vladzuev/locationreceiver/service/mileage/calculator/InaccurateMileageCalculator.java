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
@ConditionalOnProperty(prefix = "mileage-calculator", name = "enable-accurate-calculator", havingValue = "false")
public final class InaccurateMileageCalculator extends MileageCalculator {

    public InaccurateMileageCalculator(final SimplifyingCoordinatesService simplifyingCoordinatesService,
                                       final GeometryCreatingService geometryCreatingService,
                                       final CalculatingDistanceService calculatingDistanceService,
                                       final AddressService addressService) {
        super(simplifyingCoordinatesService, geometryCreatingService, calculatingDistanceService, addressService);
    }

    @Override
    protected Stream<TrackSlice> createTrackSlices(final TrackSlicesCreatingContext context) {
        final TrackSlice trackSlice = createTrackSlice(context);
        return Stream.of(trackSlice);
    }

    private static TrackSlice createTrackSlice(final TrackSlicesCreatingContext context) {
        return new TrackSlice(
                context.getFirstCoordinate(),
                context.getSecondCoordinate(),
                isAnyGeometryContainSecondPoint(context)
        );
    }

    private static boolean isAnyGeometryContainSecondPoint(final TrackSlicesCreatingContext context) {
        return context.getCityGeometries()
                .stream()
                .anyMatch(geometry -> geometry.contains(context.getSecondPoint()));
    }
}
