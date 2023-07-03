package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public final class StartingSearchingCitiesProcessServiceTest extends AbstractContextTest {

    @MockBean
    private SearchingCitiesProcessFactory mockedProcessFactory;

    @MockBean
    private SearchingCitiesProcessService mockedSearchingCitiesProcessService;

    @MockBean
    private ApplicationEventPublisher mockedEventPublisher;

    @MockBean
    private SearchingCitiesService mockedSearchingCitiesService;

    @Autowired
    private StartingSearchingCitiesProcessService startingSearchingCitiesProcessService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void processShouldBeStartedAndFinishedSuccessfully() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(0., 0.),
                new Coordinate(2., 2.)
        );
        final double givenSearchStep = 1.;

        final SearchingCitiesProcess givenCreatedProcess = SearchingCitiesProcess.builder()
                .bounds(createPolygon(this.geometryFactory, 0., 0., 0., 10., 10., 10., 10., 0.))
                .searchStep(givenSearchStep)
                .totalPoints(9)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        when(this.mockedProcessFactory.create(eq(givenAreaCoordinate), eq(givenSearchStep)))
                .thenReturn(givenCreatedProcess);

        final SearchingCitiesProcess givenSavedProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 0., 0., 0., 10., 10., 10., 10., 0.))
                .searchStep(givenSearchStep)
                .totalPoints(9)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        when(this.mockedSearchingCitiesProcessService.save(eq(givenSavedProcess))).thenReturn(givenSavedProcess);

        final List<Coordinate> expectedFirstSubtaskCoordinates = List.of(
                new Coordinate(0., 0.),
                new Coordinate(1., 0.),
                new Coordinate(2., 0.),
                new Coordinate(0., 1.)
        );
        final List<Coordinate> expectedSecondSubtaskCoordinates = List.of(
                new Coordinate(1., 1.),
                new Coordinate(2., 1.),
                new Coordinate(0., 2.),
                new Coordinate(1., 2.)
        );
        final List<Coordinate> expectedThirdSubtaskCoordinates = List.of(
                new Coordinate(2., 2.)
        );

        throw new RuntimeException();
    }

}
