package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.SearchingCitiesException;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.SubtaskSearchingCities;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.TaskSearchingAllCities;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.FailedSearchingCitiesBySubtaskEvent;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.SearchingCitiesProcessEvent;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.SuccessSearchingCitiesBySubtaskEvent;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import by.bsu.wialontransport.service.searchingcities.threadpoolexecutor.SearchingCitiesThreadPoolExecutor;
import nl.altindag.log.LogCaptor;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class StartingSearchingCitiesProcessServiceTest extends AbstractContextTest {
    private static final int GIVEN_HANDLED_POINT_COUNT_TO_SAVE_STATE = 3;

    @MockBean
    private SearchingCitiesProcessFactory mockedProcessFactory;

    @MockBean
    private SearchingCitiesProcessService mockedProcessService;

    @MockBean
    private ApplicationEventPublisher mockedEventPublisher;

    @MockBean
    private SearchingCitiesService mockedSearchingCitiesService;

    @MockBean
    private SearchingCitiesThreadPoolExecutor mockedThreadPoolExecutor;

    @Autowired
    private StartingSearchingCitiesProcessService startingProcessService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Captor
    private ArgumentCaptor<SearchingCitiesProcessEvent> eventArgumentCaptor;

    @Test
    public void subtaskShouldSuccessfullySearchCities() {
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1.1, 1.1),
                new Coordinate(2.2, 2.2),
                new Coordinate(3.3, 3.3)
        );
        final SearchingCitiesProcess givenProcess = createProcess(255L);
        final SubtaskSearchingCities givenSubtask = startingProcessService.new SubtaskSearchingCities(
                givenCoordinates,
                givenProcess
        );

        final List<City> givenCities = List.of(
                createCity(256L),
                createCity(257L)
        );
        when(mockedSearchingCitiesService.findByCoordinates(same(givenCoordinates))).thenReturn(givenCities);

        final List<City> actual = givenSubtask.search();
        assertSame(givenCities, actual);

        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        final SuccessSearchingCitiesBySubtaskEvent actualEvent = getCapturedEvent(
                SuccessSearchingCitiesBySubtaskEvent.class
        );

        final Object actualEventSource = actualEvent.getSource();
        assertSame(startingProcessService, actualEventSource);

        final SearchingCitiesProcess actualEventProcess = actualEvent.getProcess();
        assertSame(givenProcess, actualEventProcess);

        final long actualEventCountHandledPoints = actualEvent.getCountHandledPoints();
        final long expectedEventCountHandledPoints = givenCoordinates.size();
        assertEquals(expectedEventCountHandledPoints, actualEventCountHandledPoints);
    }

    @Test
    public void subtaskShouldFailSearchingCities() {
        final List<Coordinate> givenCoordinates = List.of(
                new Coordinate(1.1, 1.1),
                new Coordinate(2.2, 2.2),
                new Coordinate(3.3, 3.3)
        );
        final SearchingCitiesProcess givenProcess = createProcess(256L);
        final SubtaskSearchingCities givenSubtask = startingProcessService.new SubtaskSearchingCities(
                givenCoordinates,
                givenProcess
        );

        final Exception givenException = mock(RuntimeException.class);
        when(mockedSearchingCitiesService.findByCoordinates(same(givenCoordinates))).thenThrow(givenException);

        boolean exceptionArisen = false;
        try {
            givenSubtask.search();
        } catch (final SearchingCitiesException exception) {
            exceptionArisen = true;
            assertSame(givenException, exception.getCause());
        }
        assertTrue(exceptionArisen);

        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        final FailedSearchingCitiesBySubtaskEvent actualEvent = getCapturedEvent(
                FailedSearchingCitiesBySubtaskEvent.class
        );

        final Object actualEventSource = actualEvent.getSource();
        assertSame(startingProcessService, actualEventSource);

        final Exception actualEventException = actualEvent.getException();
        assertSame(givenException, actualEventException);
    }

    @Test
    public void taskShouldSuccessfullySearchAllCities() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(5., 5.),
                new Coordinate(6.2, 6.2)
        );
        final double givenSearchStep = 0.5;

        final long givenTotalPoints = 9;
        final SearchingCitiesProcess givenProcess = createProcess(255L, givenTotalPoints);

        final TaskSearchingAllCities givenTask = startingProcessService.new TaskSearchingAllCities(
                givenAreaCoordinate,
                givenSearchStep,
                givenProcess
        );

        final City firstGivenCity = createCity(
                createPolygon(
                        geometryFactory,
                        1, 1, 2, 2, 3, 3
                )
        );
        final City secondGivenCity = createCity(
                createPolygon(
                        geometryFactory,
                        1, 1, 3, 3, 5, 5
                )
        );
        final City thirdGivenCity = createCity(
                createPolygon(
                        geometryFactory,
                        2, 2, 4, 5, 6, 6
                )
        );
        final City fourthGivenCity = createCity(
                createPolygon(
                        geometryFactory,
                        3, 3, 6, 6, 8, 8
                )
        );

        final List<Coordinate> expectedFirstSubtaskCoordinates = List.of(
                new Coordinate(5., 5.),
                new Coordinate(5.5, 5.),
                new Coordinate(6., 5.)
        );
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedFirstSubtaskCoordinates)))
                .thenReturn(List.of(firstGivenCity, secondGivenCity, thirdGivenCity));

        final List<Coordinate> expectedSecondSubtaskCoordinates = List.of(
                new Coordinate(5., 5.5),
                new Coordinate(5.5, 5.5),
                new Coordinate(6., 5.5)
        );
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedSecondSubtaskCoordinates)))
                .thenReturn(List.of(secondGivenCity, thirdGivenCity, fourthGivenCity));

        final List<Coordinate> expectedThirdSubtaskCoordinates = List.of(
                new Coordinate(5., 6.),
                new Coordinate(5.5, 6.),
                new Coordinate(6., 6.)
        );
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedThirdSubtaskCoordinates)))
                .thenReturn(List.of(thirdGivenCity, fourthGivenCity, firstGivenCity));

        throw new RuntimeException();
    }

    private static SearchingCitiesProcess createProcess(final Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }

    private static SearchingCitiesProcess createProcess(final Long id, final long totalPoints) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .totalPoints(totalPoints)
                .build();
    }

    private static City createCity(final Long id) {
        return City.builder()
                .id(id)
                .build();
    }

    private static City createCity(final Geometry geometry) {
        return City.builder()
                .address(createAddress(geometry))
                .build();
    }

    private static Address createAddress(final Geometry geometry) {
        return Address.builder()
                .geometry(geometry)
                .build();
    }

    private <E extends SearchingCitiesProcessEvent> E getCapturedEvent(final Class<E> expectedEventType) {
        return expectedEventType.cast(eventArgumentCaptor.getValue());
    }
}
