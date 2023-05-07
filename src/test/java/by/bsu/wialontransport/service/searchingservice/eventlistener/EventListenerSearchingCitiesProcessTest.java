package by.bsu.wialontransport.service.searchingservice.eventlistener;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.eventlistener.EventListenerSearchingCitiesProcess;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.*;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public final class EventListenerSearchingCitiesProcessTest extends AbstractContextTest {
    private static final String FIELD_NAME_LOGGER = "log";
    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";

    @MockBean
    private SearchingCitiesProcessService mockedSearchingCitiesProcessService;

    @MockBean
    private CityService mockedCityService;

    @MockBean
    private AddressService mockedAddressService;

    @Mock
    private Logger mockedLogger;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<SearchingCitiesProcess> processArgumentCaptor;

    @Captor
    private ArgumentCaptor<Geometry> geometryArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<City>> citiesArgumentCaptor;

    @Captor
    private ArgumentCaptor<Status> processStatusArgumentCaptor;

    @Autowired
    private EventListenerSearchingCitiesProcess eventListener;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private StartingSearchingCitiesProcessService startingSearchingCitiesProcessService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Before
    public void injectMockedLogger()
            throws Exception {
        final Field unsafeField = Unsafe.class.getDeclaredField(FIELD_NAME_THE_UNSAFE);
        unsafeField.setAccessible(true);
        try {
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Field ourField = EventListenerSearchingCitiesProcess.class.getDeclaredField(FIELD_NAME_LOGGER);
            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, this.mockedLogger);
        } finally {
            unsafeField.setAccessible(true);
        }
    }

    @Test
    public void startSearchingCitiesProcessEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final StartSearchingCitiesProcessEvent givenEvent = new StartSearchingCitiesProcessEvent(
                this.startingSearchingCitiesProcessService, givenProcess
        );

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedLogger, times(1)).info(
                this.stringArgumentCaptor.capture(), this.processArgumentCaptor.capture()
        );
        assertEquals(
                "Process searching cities has been started. Process: {}",
                this.stringArgumentCaptor.getValue()
        );
        assertEquals(givenProcess, this.processArgumentCaptor.getValue());
    }

    @Test
    public void successSearchingCitiesBySubtaskEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final long givenAmountHandledPoints = 100;
        final SuccessSearchingCitiesBySubtaskEvent givenEvent = new SuccessSearchingCitiesBySubtaskEvent(
                this.startingSearchingCitiesProcessService,
                givenProcess,
                givenAmountHandledPoints
        );

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedSearchingCitiesProcessService, times(1)).increaseHandledPoints(
                this.processArgumentCaptor.capture(), this.longArgumentCaptor.capture()
        );
        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());

        assertEquals(givenProcess, this.processArgumentCaptor.getValue());
        assertEquals(givenAmountHandledPoints, this.longArgumentCaptor.getValue().longValue());
        assertEquals(
                "Subtask searching cities has been finished successfully.",
                this.stringArgumentCaptor.getValue()
        );
    }

    @Test
    public void failedSearchingCitiesBySubtaskEvent() {
        final String givenExceptionMessage = "message";
        final Exception givenException = mock(Exception.class);
        when(givenException.getMessage()).thenReturn(givenExceptionMessage);
        final FailedSearchingCitiesBySubtaskEvent givenEvent = new FailedSearchingCitiesBySubtaskEvent(
                this.startingSearchingCitiesProcessService,
                givenException
        );

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedLogger, times(1)).error(
                this.stringArgumentCaptor.capture(), this.stringArgumentCaptor.capture()
        );
        verify(givenException, times(1)).printStackTrace();

        assertEquals(
                List.of("Subtask searching cities has been failed. Exception: {}.", "message"),
                this.stringArgumentCaptor.getAllValues()
        );
    }

    @Test
    public void successSearchingAllCitiesEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();

        final Geometry firstGivenGeometry = createPolygon(
                this.geometryFactory,
                1, 1, 2, 2, 3, 3, 4, 4
        );
        final Geometry secondGivenGeometry = createPolygon(
                this.geometryFactory,
                5, 5, 6, 6, 7, 7, 8, 8
        );
        final Geometry thirdGivenGeometry = createPolygon(
                this.geometryFactory,
                9, 9, 10, 10, 11, 11, 12, 12
        );
        final List<City> givenFoundCities = List.of(
                createCity(256L, firstGivenGeometry),
                createCity(257L, secondGivenGeometry),
                createCity(258L, thirdGivenGeometry)
        );
        final SuccessSearchingAllCitiesEvent givenEvent = new SuccessSearchingAllCitiesEvent(
                this.startingSearchingCitiesProcessService,
                givenProcess,
                givenFoundCities
        );

        when(this.mockedAddressService.isExistByGeometry(any(Geometry.class)))
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedAddressService, times(3))
                .isExistByGeometry(this.geometryArgumentCaptor.capture());
        verify(this.mockedCityService, times(1)).saveAll(this.citiesArgumentCaptor.capture());
        verify(this.mockedSearchingCitiesProcessService, times(1)).updateStatus(
                this.processArgumentCaptor.capture(), this.processStatusArgumentCaptor.capture()
        );
        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());

        assertEquals(
                List.of(firstGivenGeometry, secondGivenGeometry, thirdGivenGeometry),
                this.geometryArgumentCaptor.getAllValues()
        );

        final List<City> expectedSavedCities = List.of(
                createCity(256L, firstGivenGeometry, givenProcess),
                createCity(257L, secondGivenGeometry, givenProcess)
        );
        assertEquals(expectedSavedCities, this.citiesArgumentCaptor.getValue());

        assertEquals(givenProcess, this.processArgumentCaptor.getValue());
        assertSame(SUCCESS, this.processStatusArgumentCaptor.getValue());
        assertEquals(
                "Process searching all cities has been finished successfully.",
                this.stringArgumentCaptor.getValue()
        );
    }

    @Test
    public void failedSearchingAllCitiesEventShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final String givenExceptionMessage = "message";
        final Exception givenException = mock(Exception.class);
        when(givenException.getMessage()).thenReturn(givenExceptionMessage);

        final FailedSearchingAllCitiesEvent givenEvent = new FailedSearchingAllCitiesEvent(
                this.startingSearchingCitiesProcessService, givenProcess, givenException
        );

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedSearchingCitiesProcessService, times(1)).updateStatus(
                this.processArgumentCaptor.capture(), this.processStatusArgumentCaptor.capture()
        );
        verify(this.mockedLogger, times(1)).error(
                this.stringArgumentCaptor.capture(), this.stringArgumentCaptor.capture()
        );
        verify(givenException, times(1)).printStackTrace();

        assertEquals(givenProcess, this.processArgumentCaptor.getValue());
        assertSame(ERROR, this.processStatusArgumentCaptor.getValue());
        assertEquals(
                List.of("Process searching all cities has been failed. Exception: {}.", givenExceptionMessage),
                this.stringArgumentCaptor.getAllValues()
        );
    }

    private static City createCity(final Long id, final Geometry geometry) {
        return City.cityBuilder()
                .id(id)
                .geometry(geometry)
                .build();
    }

    private static City createCity(final Long id, final Geometry geometry, final SearchingCitiesProcess process) {
        return City.cityBuilder()
                .id(id)
                .geometry(geometry)
                .searchingCitiesProcess(process)
                .build();
    }
}
