package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingCoordinatesService;
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
