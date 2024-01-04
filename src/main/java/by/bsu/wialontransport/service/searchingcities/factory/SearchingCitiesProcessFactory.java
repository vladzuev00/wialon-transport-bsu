package by.bsu.wialontransport.service.searchingcities.factory;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.model.AreaCoordinateRequest;
import by.bsu.wialontransport.model.RequestCoordinate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.UP;

@Component
@RequiredArgsConstructor
public final class SearchingCitiesProcessFactory {
    private final GeometryFactory geometryFactory;

    public SearchingCitiesProcess create(final AreaCoordinateRequest areaCoordinate, final double searchStep) {
        return SearchingCitiesProcess.builder()
                .bounds(this.findBounds(areaCoordinate))
                .searchStep(searchStep)
                .totalPoints(findTotalPoints(areaCoordinate, searchStep))
                .status(HANDLING)
                .build();
    }

    private static long findTotalPoints(final AreaCoordinateRequest areaCoordinate, final double searchStep) {
        return findTotalPointsInBottomSide(areaCoordinate, searchStep)
                * findTotalPointsInLeftSide(areaCoordinate, searchStep);
    }

    private static long findTotalPointsInBottomSide(final AreaCoordinateRequest areaCoordinate, final double searchStep) {
        return findTotalPointsInLine(
                areaCoordinate.getLeftBottom().getLatitude(),
                areaCoordinate.getRightUpper().getLatitude(),
                searchStep
        );
    }

    private static long findTotalPointsInLeftSide(final AreaCoordinateRequest areaCoordinate, final double searchStep) {
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

    private Geometry findBounds(final AreaCoordinateRequest areaCoordinate) {
        final CoordinateXY leftBottom = findLeftBottom(areaCoordinate);
        return this.geometryFactory.createPolygon(new CoordinateXY[]{
                leftBottom,
                findLeftUpper(areaCoordinate),
                findRightUpper(areaCoordinate),
                findRightBottom(areaCoordinate),
                leftBottom
        });
    }

    private static CoordinateXY findLeftBottom(final AreaCoordinateRequest areaCoordinate) {
        final RequestCoordinate leftBottom = areaCoordinate.getLeftBottom();
        return new CoordinateXY(leftBottom.getLatitude(), leftBottom.getLongitude());
    }

    private static CoordinateXY findLeftUpper(AreaCoordinateRequest areaCoordinate) {
        final RequestCoordinate leftBottom = areaCoordinate.getLeftBottom();
        final RequestCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(leftBottom.getLatitude(), rightUpper.getLongitude());
    }

    private static CoordinateXY findRightUpper(AreaCoordinateRequest areaCoordinate) {
        final RequestCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(rightUpper.getLatitude(), rightUpper.getLongitude());
    }

    private static CoordinateXY findRightBottom(AreaCoordinateRequest areaCoordinate) {
        final RequestCoordinate leftBottom = areaCoordinate.getLeftBottom();
        final RequestCoordinate rightUpper = areaCoordinate.getRightUpper();
        return new CoordinateXY(rightUpper.getLatitude(), leftBottom.getLongitude());
    }
}
