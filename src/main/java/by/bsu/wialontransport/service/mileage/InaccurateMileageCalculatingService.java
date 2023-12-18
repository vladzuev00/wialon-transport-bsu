package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.IntStream.rangeClosed;

@Service
public final class InaccurateMileageCalculatingService extends MileageCalculatingService {

    public InaccurateMileageCalculatingService(final SimplifyingTrackService simplifyingTrackService,
                                               final GeometryCreatingService geometryCreatingService,
                                               final CalculatingDistanceService calculatingDistanceService,
                                               final AddressService addressService) {
        super(simplifyingTrackService, geometryCreatingService, calculatingDistanceService, addressService);
    }

    @Override
    protected Stream<TrackSlice> createTrackSliceStream(final Track track,
                                                        final List<PreparedGeometry> cityGeometries,
                                                        final GeometryCreatingService geometryCreatingService) {
        final List<RequestCoordinate> trackCoordinates = track.getCoordinates();
        final int indexPenultimateCoordinate = trackCoordinates.size() - 2;
        return rangeClosed(0, indexPenultimateCoordinate)
                .mapToObj(i -> new TrackSlice(
                        trackCoordinates.get(i),
                        trackCoordinates.get(i + 1),
                        //slices, which is located in city, must have second coordinate, which is located in city
                        isAnyGeometryContainCoordinate(
                                trackCoordinates.get(i + 1),
                                cityGeometries,
                                geometryCreatingService
                        )
                ));
    }

    private static boolean isAnyGeometryContainCoordinate(final RequestCoordinate coordinate,
                                                          final List<PreparedGeometry> cityGeometries,
                                                          final GeometryCreatingService geometryCreatingService) {
        final Point point = geometryCreatingService.createPoint(coordinate);
        return cityGeometries.stream()
                .anyMatch(geometry -> geometry.contains(point));
    }
}
