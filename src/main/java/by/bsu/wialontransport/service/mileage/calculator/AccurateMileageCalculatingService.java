//package by.bsu.wialontransport.service.mileage;
//
//import by.bsu.wialontransport.crud.service.AddressService;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.model.Track;
//import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
//import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
//import by.bsu.wialontransport.service.mileage.model.TrackSlice;
//import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
//import by.bsu.wialontransport.util.CoordinateUtil;
//import lombok.Value;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.LineString;
//import org.locationtech.jts.geom.prep.PreparedGeometry;
//
//import java.util.List;
//import java.util.function.Function;
//import java.util.function.ToIntFunction;
//import java.util.stream.Stream;
//
//import static by.bsu.wialontransport.util.CollectionUtil.mapToList;
//import static by.bsu.wialontransport.util.StreamUtil.concat;
//import static java.util.stream.IntStream.range;
//
//
//public final class AccurateMileageCalculatingService extends AbstractMileageCalculatingService {
//
//    public AccurateMileageCalculatingService(final SimplifyingTrackService simplifyingTrackService,
//                                             final GeometryCreatingService geometryCreatingService,
//                                             final CalculatingDistanceService calculatingDistanceService,
//                                             final AddressService addressService) {
//        super(simplifyingTrackService, geometryCreatingService, calculatingDistanceService, addressService);
//    }
//
//    @Override
//    protected Stream<TrackSlice> createTrackSliceStream(final Track track,
//                                                        final List<PreparedGeometry> cityPreparedGeometries,
//                                                        final GeometryCreatingService geometryCreatingService) {
//        final List<Geometry> cityGeometries = mapToList(cityPreparedGeometries, PreparedGeometry::getGeometry);
//        return findSnippets(track, geometryCreatingService)
//                .flatMap(snippet -> splitSnippetIntoTrackSlices(snippet, cityGeometries));
//    }
//
//    private static Stream<LineString> findSnippets(final Track track,
//                                                   final GeometryCreatingService geometryCreatingService) {
//        final List<Coordinate> coordinates = track.getCoordinates();
//        final int penultimateCoordinateIndex = coordinates.size() - 2;
//        return range(0, penultimateCoordinateIndex)
//                .mapToObj(
//                        i -> geometryCreatingService.createLineString(
//                                coordinates.get(i),
//                                coordinates.get(i + 1)
//                        )
//                );
//    }
//
//    private static Stream<TrackSlice> splitSnippetIntoTrackSlices(final LineString snippet,
//                                                                  final List<Geometry> cityGeometries) {
//        final List<TrackSlicePoint> trackSlicePoints = findTrackSlicePoints(snippet, cityGeometries);
//        final int penultimatePointIndex = trackSlicePoints.size() - 2;
//        return range(0, penultimatePointIndex)
//                .mapToObj(
//                        i -> createTrackSlice(
//                                trackSlicePoints.get(i),
//                                trackSlicePoints.get(i + 1)
//                        )
//                );
//    }
//
//    private static List<TrackSlicePoint> findTrackSlicePoints(final LineString snippet,
//                                                              final List<Geometry> cityGeometries) {
//
//    }
//
//    private static TrackSlice createTrackSlice(final TrackSlicePoint firstPoint, final TrackSlicePoint secondPoint) {
//        final boolean locatedInCity = firstPoint.isStartCityTrackSlice();
//        final Coordinate firstCoordinate = firstPoint.getCoordinate();
//        final Coordinate secondCoordinate = secondPoint.getCoordinate();
//        return new TrackSlice(firstCoordinate, secondCoordinate, locatedInCity);
//    }
//
//    private static List<Coordinate> TEMPfindTrackSliceBorderCoordinates(final LineString snippet,
//                                                                        final List<Geometry> cityGeometries) {
//        return concat(
//                findStartCoordinateWrappedByStream(snippet),
//                findIntersectionBorderCoordinates(snippet, cityGeometries),
//                findFinishCoordinateWrappedByStream(snippet)
//        ).map(CoordinateUtil::mapToCoordinate).toList();
//    }
//
//    private static Stream<org.locationtech.jts.geom.Coordinate> findIntersectionBorderCoordinates(final LineString snippet,
//                                                                                                  final List<Geometry> cityGeometries) {
//        return cityGeometries.stream()
//                //TODO: посмотреть что возвращает intersection
//                .map(snippet::intersection)
//                .flatMap(AccurateMileageCalculatingService::findBorderCoordinates);
//    }
//
//    private static Stream<org.locationtech.jts.geom.Coordinate> findBorderCoordinates(final Geometry geometry) {
//        return Stream.of(
//                findStartCoordinate(geometry),
//                findFinishCoordinate(geometry)
//        );
//    }
//
//    private static org.locationtech.jts.geom.Coordinate findStartCoordinate(final Geometry geometry) {
//        return findCoordinate(
//                geometry,
//                coordinates -> 0
//        );
//    }
//
//    private static org.locationtech.jts.geom.Coordinate findFinishCoordinate(final Geometry geometry) {
//        return findCoordinate(
//                geometry,
//                coordinates -> coordinates.length - 1
//        );
//    }
//
//    private static org.locationtech.jts.geom.Coordinate findCoordinate(final Geometry geometry,
//                                                                       final ToIntFunction<org.locationtech.jts.geom.Coordinate[]> coordinateIndexExtractor) {
//        final org.locationtech.jts.geom.Coordinate[] coordinates = geometry.getCoordinates();
//        final int coordinateIndex = coordinateIndexExtractor.applyAsInt(coordinates);
//        return coordinates[coordinateIndex];
//    }
//
//    private static Stream<org.locationtech.jts.geom.Coordinate> findStartCoordinateWrappedByStream(final Geometry geometry) {
//        return findCoordinateWrappedByStream(
//                geometry,
//                AccurateMileageCalculatingService::findStartCoordinate
//        );
//    }
//
//    private static Stream<org.locationtech.jts.geom.Coordinate> findFinishCoordinateWrappedByStream(final Geometry geometry) {
//        return findCoordinateWrappedByStream(
//                geometry,
//                AccurateMileageCalculatingService::findFinishCoordinate
//        );
//    }
//
//    private static Stream<org.locationtech.jts.geom.Coordinate> findCoordinateWrappedByStream(final Geometry geometry,
//                                                                                              final Function<Geometry, org.locationtech.jts.geom.Coordinate> coordinateExtractor) {
//        final org.locationtech.jts.geom.Coordinate coordinate = coordinateExtractor.apply(geometry);
//        return Stream.of(coordinate);
//    }
//
//    @Value
//    private static class TrackSlicePoint {
//        Coordinate coordinate;
//        boolean startCityTrackSlice;
//    }
//}
