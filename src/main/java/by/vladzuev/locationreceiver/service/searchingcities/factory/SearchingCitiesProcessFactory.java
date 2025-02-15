package by.vladzuev.locationreceiver.service.searchingcities.factory;

import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.model.AreaCoordinate;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.UP;

@Component
@RequiredArgsConstructor
public final class SearchingCitiesProcessFactory {
    private final GeometryFactory geometryFactory;

    public SearchingCitiesProcess create(final AreaCoordinate areaCoordinate, final double searchStep) {
        return SearchingCitiesProcess.builder()
                .bounds(findBounds(areaCoordinate))
                .searchStep(searchStep)
                .totalPoints(findTotalPoints(areaCoordinate, searchStep))
                .status(HANDLING)
                .build();
    }

    private static long findTotalPoints(final AreaCoordinate areaCoordinate, final double searchStep) {
        return findTotalPointsInBottomSide(areaCoordinate, searchStep)
                * findTotalPointsInLeftSide(areaCoordinate, searchStep);
    }

    private static long findTotalPointsInBottomSide(final AreaCoordinate areaCoordinate, final double searchStep) {
        return findTotalPointsInLine(
                areaCoordinate.getLeftBottom().getLatitude(),
                areaCoordinate.getRightUpper().getLatitude(),
                searchStep
        );
    }

    private static long findTotalPointsInLeftSide(final AreaCoordinate areaCoordinate, final double searchStep) {
        return findTotalPointsInLine(
                areaCoordinate.getLeftBottom().getLongitude(),
                areaCoordinate.getRightUpper().getLongitude(),
                searchStep
        );
    }

    private static long findTotalPointsInLine(final double startCoordinate,
                                              final double endCoordinate,
                                              final double searchStep) {
        return valueOf(endCoordinate)
                .subtract(valueOf(startCoordinate))
                .divide(valueOf(searchStep), UP)
                .longValue() + 1;
    }

    private Geometry findBounds(final AreaCoordinate areaCoordinate) {
        final CoordinateXY leftBottom = findLeftBottom(areaCoordinate);
        return geometryFactory.createPolygon(new CoordinateXY[]{
                leftBottom,
                findLeftUpper(areaCoordinate),
                findRightUpper(areaCoordinate),
                findRightBottom(areaCoordinate),
                leftBottom
        });
    }

    private static CoordinateXY findLeftBottom(final AreaCoordinate areaCoordinate) {
        final GpsCoordinate leftBottom = areaCoordinate.getLeftBottom();
        return new CoordinateXY(leftBottom.getLatitude(), leftBottom.getLongitude());
    }

    private static CoordinateXY findLeftUpper(final AreaCoordinate areaCoordinate) {
        final GpsCoordinate leftBottom = areaCoordinate.getLeftBottom();
        final GpsCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(leftBottom.getLatitude(), rightUpper.getLongitude());
    }

    private static CoordinateXY findRightUpper(final AreaCoordinate areaCoordinate) {
        final GpsCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(rightUpper.getLatitude(), rightUpper.getLongitude());
    }

    private static CoordinateXY findRightBottom(final AreaCoordinate areaCoordinate) {
        final GpsCoordinate leftBottom = areaCoordinate.getLeftBottom();
        final GpsCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(rightUpper.getLatitude(), leftBottom.getLongitude());
    }
}
