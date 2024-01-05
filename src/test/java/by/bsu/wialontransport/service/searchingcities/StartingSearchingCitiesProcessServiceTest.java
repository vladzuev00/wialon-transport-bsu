package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.SearchingCitiesException;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.SubtaskSearchingCities;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService.TaskSearchingAllCities;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import by.bsu.wialontransport.service.searchingcities.threadpoolexecutor.SearchingCitiesThreadPoolExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static by.bsu.wialontransport.util.CollectionUtil.areAllMatch;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class StartingSearchingCitiesProcessServiceTest {
    private static final int GIVEN_THREAD_POOL_EXECUTOR_THREAD_COUNT = 2;
    private static final int GIVEN_HANDLED_POINT_COUNT_TO_SAVE_STATE = 3;

    private static final int GEOMETRY_FACTORY_SRID = 4326;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), GEOMETRY_FACTORY_SRID);

    @Mock
    private SearchingCitiesProcessFactory mockedProcessFactory;

    @Mock
    private SearchingCitiesProcessService mockedProcessService;

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    @Mock
    private SearchingCitiesService mockedSearchingCitiesService;

    private final SearchingCitiesThreadPoolExecutor threadPoolExecutor = new SearchingCitiesThreadPoolExecutor(
            GIVEN_THREAD_POOL_EXECUTOR_THREAD_COUNT
    );

    private StartingSearchingCitiesProcessService startingProcessService;

    @Captor
    private ArgumentCaptor<SearchingCitiesProcessEvent> eventArgumentCaptor;

    @Captor
    private ArgumentCaptor<TaskSearchingAllCities> taskArgumentCaptor;

    @Before
    public void initializeStartingProcessService() {
        startingProcessService = new StartingSearchingCitiesProcessService(
                mockedProcessFactory,
                mockedProcessService,
                mockedEventPublisher,
                mockedSearchingCitiesService,
                threadPoolExecutor,
                GIVEN_HANDLED_POINT_COUNT_TO_SAVE_STATE
        );
    }

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

        final List<City> actual = givenSubtask.get();
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
            givenSubtask.get();
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
        final List<City> givenFirstSubtaskCities = List.of(firstGivenCity, secondGivenCity, thirdGivenCity);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedFirstSubtaskCoordinates)))
                .thenReturn(givenFirstSubtaskCities);

        final List<Coordinate> expectedSecondSubtaskCoordinates = List.of(
                new Coordinate(5., 5.5),
                new Coordinate(5.5, 5.5),
                new Coordinate(6., 5.5)
        );
        final List<City> givenSecondSubtaskCities = List.of(secondGivenCity, thirdGivenCity, fourthGivenCity);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedSecondSubtaskCoordinates)))
                .thenReturn(givenSecondSubtaskCities);

        final List<Coordinate> expectedThirdSubtaskCoordinates = List.of(
                new Coordinate(5., 6.),
                new Coordinate(5.5, 6.),
                new Coordinate(6., 6.)
        );
        final List<City> givenThirdSubtaskCities = List.of(thirdGivenCity, fourthGivenCity, firstGivenCity);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedThirdSubtaskCoordinates)))
                .thenReturn(givenThirdSubtaskCities);

        givenTask.run();

        verify(mockedEventPublisher, times(4)).publishEvent(eventArgumentCaptor.capture());
        final List<SearchingCitiesProcessEvent> actualCapturedEvents = eventArgumentCaptor.getAllValues();

        final List<SearchingCitiesProcessEvent> actualCapturedFirstThreeEvents = actualCapturedEvents.subList(0, 3);
        final boolean subtaskSearchingSuccess = areAllMatch(
                actualCapturedFirstThreeEvents,
                SuccessSearchingCitiesBySubtaskEvent.class::isInstance
        );
        assertTrue(subtaskSearchingSuccess);

        final SuccessSearchingAllCitiesEvent actualFourthEvent = (SuccessSearchingAllCitiesEvent)
                actualCapturedEvents.get(3);

        final Object actualEventSource = actualFourthEvent.getSource();
        assertSame(startingProcessService, actualEventSource);

        final SearchingCitiesProcess actualEventProcess = actualFourthEvent.getProcess();
        assertSame(givenProcess, actualEventProcess);

        final Collection<City> actualEventFoundCities = new HashSet<>(actualFourthEvent.getFoundCities());
        final Set<City> expectedEventFoundCities = Set.of(
                firstGivenCity,
                secondGivenCity,
                thirdGivenCity,
                fourthGivenCity
        );
        assertEquals(expectedEventFoundCities, actualEventFoundCities);
    }

    @Test
    public void taskShouldFailSearchingCities() {
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
        final List<City> givenFirstSubtaskCities = List.of(firstGivenCity, secondGivenCity, thirdGivenCity);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedFirstSubtaskCoordinates)))
                .thenReturn(givenFirstSubtaskCities);

        final List<Coordinate> expectedSecondSubtaskCoordinates = List.of(
                new Coordinate(5., 5.5),
                new Coordinate(5.5, 5.5),
                new Coordinate(6., 5.5)
        );
        final Exception givenException = mock(RuntimeException.class);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedSecondSubtaskCoordinates)))
                .thenThrow(givenException);

        final List<Coordinate> expectedThirdSubtaskCoordinates = List.of(
                new Coordinate(5., 6.),
                new Coordinate(5.5, 6.),
                new Coordinate(6., 6.)
        );
        final List<City> givenThirdSubtaskCities = List.of(thirdGivenCity, fourthGivenCity, firstGivenCity);
        when(mockedSearchingCitiesService.findByCoordinates(eq(expectedThirdSubtaskCoordinates)))
                .thenReturn(givenThirdSubtaskCities);

        givenTask.run();

        verify(mockedEventPublisher, times(4)).publishEvent(eventArgumentCaptor.capture());
        final List<SearchingCitiesProcessEvent> actualCapturedEvents = eventArgumentCaptor.getAllValues();

        final List<SearchingCitiesProcessEvent> actualSubtaskEvents = actualCapturedEvents.subList(0, 3);
        final Map<Class<?>, Long> actualSubtaskEventCountsByTypes = countEventForEachType(actualSubtaskEvents);
        final Map<Class<?>, Long> expectedSubtaskEventCountsByTypes = Map.of(
                SuccessSearchingCitiesBySubtaskEvent.class, 2L,
                FailedSearchingCitiesBySubtaskEvent.class, 1L
        );
        assertEquals(expectedSubtaskEventCountsByTypes, actualSubtaskEventCountsByTypes);

        final FailedSearchingAllCitiesEvent actualMainTaskEvent = (FailedSearchingAllCitiesEvent)
                actualCapturedEvents.get(3);

        final Object actualMainTaskEventSource = actualMainTaskEvent.getSource();
        assertSame(startingProcessService, actualMainTaskEventSource);

        final SearchingCitiesProcess actualMainTaskEventProcess = actualMainTaskEvent.getProcess();
        assertSame(givenProcess, actualMainTaskEventProcess);

        final ExecutionException executionException = (ExecutionException) actualMainTaskEvent.getException();
        final SearchingCitiesException searchingCitiesException = (SearchingCitiesException) executionException.getCause();
        final Throwable actualMainTaskException = searchingCitiesException.getCause();
        assertSame(givenException, actualMainTaskException);
    }

    @Test
    public void processShouldBeStarted() {
        try (@SuppressWarnings("rawtypes") final MockedStatic<CompletableFuture> mockedStaticFuture = mockStatic(CompletableFuture.class)) {
            final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                    new Coordinate(1.1, 1.1),
                    new Coordinate(4.4, 4.4)
            );
            final double givenSearchStep = 0.5;

            final SearchingCitiesProcess givenProcess = createProcess();
            when(mockedProcessFactory.create(same(givenAreaCoordinate), eq(givenSearchStep))).thenReturn(givenProcess);

            final SearchingCitiesProcess givenSavedProcess = createProcess(255L);
            when(mockedProcessService.save(same(givenProcess))).thenReturn(givenSavedProcess);

            final SearchingCitiesProcess actual = startingProcessService.start(givenAreaCoordinate, givenSearchStep);
            assertSame(givenSavedProcess, actual);

            verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
            mockedStaticFuture.verify(() -> runAsync(taskArgumentCaptor.capture(), same(threadPoolExecutor)));

            final StartSearchingCitiesProcessEvent actualEvent = getCapturedEvent(
                    StartSearchingCitiesProcessEvent.class
            );

            final Object actualEventSource = actualEvent.getSource();
            assertSame(startingProcessService, actualEventSource);

            final SearchingCitiesProcess actualEventProcess = actualEvent.getProcess();
            assertSame(givenSavedProcess, actualEventProcess);

            final TaskSearchingAllCities actualTask = taskArgumentCaptor.getValue();

            final AreaCoordinate actualTaskAreaCoordinate = actualTask.getAreaCoordinate();
            assertSame(givenAreaCoordinate, actualTaskAreaCoordinate);

            final double actualTaskSearchStep = actualTask.getSearchStep();
            assertEquals(givenSearchStep, actualTaskSearchStep, 0.);

            final SearchingCitiesProcess actualTaskProcess = actualTask.getProcess();
            assertSame(givenSavedProcess, actualTaskProcess);
        }
    }

    private static SearchingCitiesProcess createProcess() {
        return SearchingCitiesProcess.builder().build();
    }

    private static SearchingCitiesProcess createProcess(final Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
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

    private static Map<Class<?>, Long> countEventForEachType(final List<SearchingCitiesProcessEvent> events) {
        return events.stream()
                .collect(
                        groupingBy(
                                Object::getClass,
                                counting()
                        )
                );
    }
}
