package by.bsu.wialontransport.service.searchingservice.eventlistener;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
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
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.*;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.util.Optional.empty;
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

    @MockBean(name = "executorServiceToSearchCities")
    private ExecutorService mockedExecutorService;

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
    private ArgumentCaptor<Address> addressArgumentCaptor;

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
            unsafeField.setAccessible(false);
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
        assertSame(givenProcess, this.processArgumentCaptor.getValue());
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

        assertSame(givenProcess, this.processArgumentCaptor.getValue());
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
        verify(this.mockedExecutorService, times(1)).shutdownNow();

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

        final Address firstGivenAddress = createAddress(256L, firstGivenGeometry);
        final Address secondGivenAddress = createAddress(257L, secondGivenGeometry);
        final Address thirdGivenAddress = createAddress(258L, thirdGivenGeometry);

        final List<City> givenFoundCities = List.of(
                //existing city with existing address
                createCity(256L, firstGivenAddress),
                //not existing city with existing address
                createCity(257L, secondGivenAddress),
                //not existing city with not existing address
                createCity(258L, thirdGivenAddress)
        );
        final SuccessSearchingAllCitiesEvent givenEvent = new SuccessSearchingAllCitiesEvent(
                this.startingSearchingCitiesProcessService,
                givenProcess,
                givenFoundCities
        );

        when(this.mockedCityService.isExistByGeometry(any(Geometry.class)))
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(false);

        when(this.mockedAddressService.findByGeometry(secondGivenGeometry))
                .thenReturn(Optional.of(secondGivenAddress));

        when(this.mockedAddressService.findByGeometry(thirdGivenGeometry)).thenReturn(empty());

        when(this.mockedAddressService.save(thirdGivenAddress)).thenReturn(thirdGivenAddress);

        this.eventPublisher.publishEvent(givenEvent);

        verify(this.mockedCityService, times(1))
                .saveAll(this.citiesArgumentCaptor.capture());
        verify(this.mockedSearchingCitiesProcessService, times(1)).updateStatus(
                this.processArgumentCaptor.capture(), this.processStatusArgumentCaptor.capture()
        );
        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());

        final List<City> expectedSavedCities = List.of(
                createCity(257L, secondGivenAddress, givenProcess),
                createCity(258L, thirdGivenAddress, givenProcess)
        );
        assertEquals(expectedSavedCities, this.citiesArgumentCaptor.getValue());

        assertSame(givenProcess, this.processArgumentCaptor.getValue());
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

    private static City createCity(final Long id, final Address address) {
        return City.builder()
                .id(id)
                .address(address)
                .build();
    }

    private static City createCity(final Long id, final Address address, final SearchingCitiesProcess process) {
        return City.builder()
                .id(id)
                .address(address)
                .searchingCitiesProcess(process)
                .build();
    }

    private static Address createAddress(final Long id, final Geometry geometry) {
        return Address.builder()
                .id(id)
                .geometry(geometry)
                .build();
    }
}
