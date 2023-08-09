package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.IntStream.rangeClosed;

@Service
public final class InaccurateMileageCalculatingService extends AbstractMileageCalculatingService {

    public InaccurateMileageCalculatingService(final SimplifyingTrackService simplifyingTrackService,
                                               final GeometryCreatingService geometryCreatingService,
                                               final CalculatingDistanceService calculatingDistanceService,
                                               final AddressService addressService) {
        super(simplifyingTrackService, geometryCreatingService, calculatingDistanceService, addressService);
    }

    @Override
    protected Stream<TrackSlice> createTrackSliceStream(final Track track, final Predicate<Coordinate> locatedInCity) {
        final List<Coordinate> trackCoordinates = track.getCoordinates();
        final int indexPenultimateCoordinate = trackCoordinates.size() - 2;
        return rangeClosed(0, indexPenultimateCoordinate)
                .mapToObj(i -> new TrackSlice(
                        trackCoordinates.get(i),
                        trackCoordinates.get(i + 1),
                        //slices, which is located in city, must have second coordinate, which is located in city
                        locatedInCity.test(trackCoordinates.get(i + 1))
                ));
    }
}
