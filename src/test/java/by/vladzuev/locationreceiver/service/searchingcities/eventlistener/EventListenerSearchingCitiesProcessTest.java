package by.vladzuev.locationreceiver.service.searchingcities.eventlistener;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.City;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.crud.service.CityService;
import by.vladzuev.locationreceiver.crud.service.SearchingCitiesProcessService;
import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
import by.vladzuev.locationreceiver.service.searchingcities.threadpoolexecutor.SearchingCitiesThreadPoolExecutor;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
import by.vladzuev.locationreceiver.service.searchingcities.eventlistener.event.*;
import nl.altindag.log.LogCaptor;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
import static java.util.Optional.empty;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class EventListenerSearchingCitiesProcessTest extends AbstractSpringBootTest {

    @MockBean
    private SearchingCitiesProcessService mockedSearchingCitiesProcessService;

    @MockBean
    private CityService mockedCityService;

    @MockBean
    private AddressService mockedAddressService;

    @MockBean
    private SearchingCitiesThreadPoolExecutor mockedThreadPoolExecutor;

    @Captor
    private ArgumentCaptor<List<City>> citiesArgumentCaptor;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private StartingSearchingCitiesProcessService startingSearchingCitiesProcessService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private LogCaptor logCaptor;

    @Before
    public void initializeLogCaptor() {
        logCaptor = forClass(EventListenerSearchingCitiesProcess.class);
    }

    @Test
    public void startSearchingCitiesEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();
        final StartSearchingCitiesProcessEvent givenEvent = new StartSearchingCitiesProcessEvent(
                startingSearchingCitiesProcessService,
                givenProcess
        );

        eventPublisher.publishEvent(givenEvent);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of(
                "Process searching cities has been started. "
                        + "Process: SearchingCitiesProcess(id=255, bounds=POLYGON ((1 1, 1 2, 2 2, 2 1, 1 1)), "
                        + "searchStep=0.5, totalPoints=100, handledPoints=0, status=HANDLING)"
        );
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void successSearchingCitiesBySubtaskEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();
        final long givenCountHandledPoints = 100;
        final SuccessSearchingCitiesBySubtaskEvent givenEvent = new SuccessSearchingCitiesBySubtaskEvent(
                startingSearchingCitiesProcessService,
                givenProcess,
                givenCountHandledPoints
        );

        eventPublisher.publishEvent(givenEvent);

        verify(mockedSearchingCitiesProcessService, times(1)).increaseHandledPoints(
                same(givenProcess),
                eq(givenCountHandledPoints)
        );

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Subtask searching cities has been finished successfully");
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void failedSearchingCitiesBySubtaskEventShouldBeHandled() {
        final String givenExceptionMessage = "message";
        final Exception givenException = createException(givenExceptionMessage);
        final FailedSearchingCitiesBySubtaskEvent givenEvent = new FailedSearchingCitiesBySubtaskEvent(
                startingSearchingCitiesProcessService,
                givenException
        );

        eventPublisher.publishEvent(givenEvent);

        verify(givenException, times(1)).printStackTrace();
        verify(mockedThreadPoolExecutor, times(1)).shutdownNow();

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Subtask searching cities has been failed. Exception: message");
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void successSearchingAllCitiesEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();

        final Geometry firstGivenGeometry = createPolygon(
                geometryFactory,
                1, 1, 2, 2, 3, 3, 4, 4
        );
        final Geometry secondGivenGeometry = createPolygon(
                geometryFactory,
                5, 5, 6, 6, 7, 7, 8, 8
        );
        final Geometry thirdGivenGeometry = createPolygon(
                geometryFactory,
                9, 9, 10, 10, 11, 11, 12, 12
        );

        final Address firstGivenAddress = createAddress(firstGivenGeometry);
        final Address secondGivenAddress = createAddress(secondGivenGeometry);
        final Address thirdGivenAddress = createAddress(thirdGivenGeometry);

        final List<City> givenFoundCities = List.of(
                createCity(firstGivenAddress),
                createCity(secondGivenAddress),
                createCity(thirdGivenAddress)
        );
        final SuccessSearchingAllCitiesEvent givenEvent = new SuccessSearchingAllCitiesEvent(
                startingSearchingCitiesProcessService,
                givenProcess,
                givenFoundCities
        );

        when(mockedCityService.isExistByGeometry(same(firstGivenGeometry))).thenReturn(true);
        when(mockedCityService.isExistByGeometry(same(secondGivenGeometry))).thenReturn(false);
        when(mockedCityService.isExistByGeometry(same(thirdGivenGeometry))).thenReturn(false);

        final Address secondGivenSavedAddress = createAddress(255L, secondGivenGeometry);
        when(mockedAddressService.findByGeometry(same(secondGivenGeometry)))
                .thenReturn(Optional.of(secondGivenSavedAddress));

        when(mockedAddressService.findByGeometry(same(thirdGivenGeometry))).thenReturn(empty());

        final Address thirdGivenSavedAddress = createAddress(256L, thirdGivenGeometry);
        when(mockedAddressService.save(same(thirdGivenAddress))).thenReturn(thirdGivenSavedAddress);

        eventPublisher.publishEvent(givenEvent);

        verify(mockedCityService, times(1)).saveAll(citiesArgumentCaptor.capture());
        verify(mockedSearchingCitiesProcessService, times(1)).updateStatus(
                same(givenProcess),
                same(SearchingCitiesProcessEntity.Status.SUCCESS)
        );

        final List<City> actualSavedCities = citiesArgumentCaptor.getValue();
        final List<City> expectedSavedCities = List.of(
                createCity(secondGivenSavedAddress, givenProcess),
                createCity(thirdGivenSavedAddress, givenProcess)
        );
        assertEquals(expectedSavedCities, actualSavedCities);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Process searching all cities has been finished successfully");
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void failedSearchingAllCitiesEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();

        final String givenExceptionMessage = "text";
        final Exception givenException = createException(givenExceptionMessage);

        final FailedSearchingAllCitiesEvent givenEvent = new FailedSearchingAllCitiesEvent(
                startingSearchingCitiesProcessService,
                givenProcess,
                givenException
        );

        eventPublisher.publishEvent(givenEvent);

        verify(mockedSearchingCitiesProcessService, times(1)).updateStatus(
                same(givenProcess),
                same(SearchingCitiesProcessEntity.Status.ERROR)
        );
        verify(givenException, times(1)).printStackTrace();

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Process searching all cities has been failed. Exception: text");
        assertEquals(expectedLogs, actualLogs);
    }

    private static Exception createException(final String message) {
        final Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn(message);
        return exception;
    }

    private static City createCity(final Address address) {
        return City.builder()
                .address(address)
                .build();
    }

    private static City createCity(final Address address, final SearchingCitiesProcess process) {
        return City.builder()
                .address(address)
                .searchingCitiesProcess(process)
                .build();
    }

    private static Address createAddress(final Geometry geometry) {
        return Address.builder()
                .geometry(geometry)
                .build();
    }

    private static Address createAddress(final Long id, final Geometry geometry) {
        return Address.builder()
                .id(id)
                .geometry(geometry)
                .build();
    }
}
