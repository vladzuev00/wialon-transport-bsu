package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;

import java.util.function.Predicate;
import java.util.stream.Stream;

public final class AccurateMileageCalculatingService extends AbstractMileageCalculatingService {


    public AccurateMileageCalculatingService(final SimplifyingTrackService simplifyingTrackService,
                                             final GeometryCreatingService geometryCreatingService,
                                             final CalculatingDistanceService calculatingDistanceService,
                                             final AddressService addressService) {
        super(simplifyingTrackService, geometryCreatingService, calculatingDistanceService, addressService);
    }

    @Override
    protected Stream<TrackSlice> createTrackSliceStream(final Track track, final Predicate<Coordinate> locatedInCity) {

        return null;
    }
}
