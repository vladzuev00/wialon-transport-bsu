package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.kafka.consumer.data.KafkaDataConsumer.ConsumingContext;
import by.bsu.wialontransport.kafka.model.view.InboundParameterView;
import by.bsu.wialontransport.kafka.producer.data.KafkaSavedDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.geocoding.GeocodingManager;
import by.bsu.wialontransport.service.mileage.MileageIncreasingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaInboundDataConsumerTest {

    @Mock
    private GeocodingManager mockedGeocodingManager;

    @Mock
    private DataService mockedDataService;

    @Mock
    private MileageIncreasingService mockedMileageIncreasingService;

    @Mock
    private KafkaSavedDataProducer mockedSavedDataProducer;

    @Captor
    private ArgumentCaptor<Track> trackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Data> dataArgumentCaptor;

    private KafkaInboundDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        consumer = new KafkaInboundDataConsumer(
                null,
                null,
                mockedGeocodingManager,
                mockedDataService,
                mockedMileageIncreasingService,
                mockedSavedDataProducer
        );
    }

    @Test
    public void dataShouldBeCreated() {
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                1,
                8,
                4,
                18,
                15
        );
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        final double givenSpeed = 26;
        final int givenCourse = 27;
        final int givenAltitude = 28;
        final int givenAmountOfSatellites = 29;
        final double givenHdop = 30.5;
        final int givenInputs = 31;
        final int givenOutputs = 32;
        final double[] givenAnalogInputs = new double[]{0.2, 0.3, 0.4};
        final String givenDriverKeyCode = "driver key code";
        final Map<String, Parameter> givenParametersByNames = Map.of(
                "first-param", createParameter(256L),
                "second-param", createParameter(257L)
        );
        final Tracker givenTracker = createTracker(258L);
        final Address givenAddress = createAddress(259L);
        final ConsumingContext givenContext = createContext(
                givenDateTime,
                givenCoordinate,
                givenSpeed,
                givenCourse,
                givenAltitude,
                givenAmountOfSatellites,
                givenHdop,
                givenInputs,
                givenOutputs,
                givenAnalogInputs,
                givenDriverKeyCode,
                givenParametersByNames,
                givenTracker,
                givenAddress
        );

        @SuppressWarnings("unchecked") final Data actual = consumer.createData(givenContext);
        final Data expected = Data.builder()
                .dateTime(givenDateTime)
                .coordinate(givenCoordinate)
                .speed(givenSpeed)
                .course(givenCourse)
                .altitude(givenAltitude)
                .amountOfSatellites(givenAmountOfSatellites)
                .hdop(givenHdop)
                .inputs(givenInputs)
                .outputs(givenOutputs)
                .analogInputs(givenAnalogInputs)
                .driverKeyCode(givenDriverKeyCode)
                .parametersByNames(givenParametersByNames)
                .tracker(givenTracker)
                .address(givenAddress)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void parameterShouldBeCreated() {
        final String givenName = "parameter-name";
        final Type givenType = STRING;
        final String givenValue = "text";
        final InboundParameterView givenView = new InboundParameterView(givenName, givenType, givenValue);

        final Parameter actual = consumer.createParameter(givenView);
        final Parameter expected = createParameter(givenName, givenType, givenValue);
        assertEquals(expected, actual);
    }

    @Test
    public void savedAddressShouldBeFound() {
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        final ConsumingContext givenContext = createContext(givenCoordinate);

        final Address givenAddress = createAddress(255L);
        when(mockedGeocodingManager.findSavedAddress(same(givenCoordinate))).thenReturn(Optional.of(givenAddress));

        @SuppressWarnings("unchecked") final Optional<Address> optionalActual = consumer.findSavedAddress(givenContext);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void savedAddressShouldNotBeFound() {
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        final ConsumingContext givenContext = createContext(givenCoordinate);

        when(mockedGeocodingManager.findSavedAddress(same(givenCoordinate))).thenReturn(empty());

        @SuppressWarnings("unchecked") final Optional<Address> optionalActual = consumer.findSavedAddress(givenContext);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void dataShouldBeProcessed() {
        final Tracker firstGivenTracker = createTracker(255L);
        final Tracker secondGivenTracker = createTracker(256L);

        final Coordinate firstGivenCoordinate = new Coordinate(1.1, 1.1);
        final Coordinate secondGivenCoordinate = new Coordinate(2.2, 2.2);
        final Coordinate thirdGivenCoordinate = new Coordinate(3.3, 3.3);
        final Coordinate fourthGivenCoordinate = new Coordinate(4.4, 4.4);
        final Coordinate fifthGivenCoordinate = new Coordinate(5.5, 5.5);

        final Data firstGivenData = createData(firstGivenCoordinate, firstGivenTracker);
        final Data secondGivenData = createData(secondGivenCoordinate, firstGivenTracker);
        final Data thirdGivenData = createData(thirdGivenCoordinate, secondGivenTracker);
        final Data fourthGivenData = createData(fourthGivenCoordinate, secondGivenTracker);
        final Data fifthGivenData = createData(fifthGivenCoordinate, secondGivenTracker);
        final List<Data> givenData = List.of(
                firstGivenData,
                secondGivenData,
                thirdGivenData,
                fourthGivenData,
                fifthGivenData
        );

        final Data firstGivenSavedData = createData(1L, firstGivenCoordinate, firstGivenTracker);
        final Data secondGivenSavedData = createData(2L, secondGivenCoordinate, firstGivenTracker);
        final Data thirdGivenSavedData = createData(3L, thirdGivenCoordinate, secondGivenTracker);
        final Data fourthGivenSavedData = createData(4L, fourthGivenCoordinate, secondGivenTracker);
        final Data fifthGivenSavedData = createData(5L, fifthGivenCoordinate, secondGivenTracker);
        final List<Data> givenSavedData = List.of(
                firstGivenSavedData,
                secondGivenSavedData,
                thirdGivenSavedData,
                fourthGivenSavedData,
                fifthGivenSavedData
        );
        when(mockedDataService.saveAll(same(givenData))).thenReturn(givenSavedData);

        consumer.process(givenData);

        verify(mockedMileageIncreasingService, times(2)).increase(trackArgumentCaptor.capture());
        final List<Track> actualCapturedTracks = trackArgumentCaptor.getAllValues();
        final List<Track> expectedCapturedTracks = List.of(
                new Track(firstGivenTracker, List.of(firstGivenCoordinate, secondGivenCoordinate)),
                new Track(secondGivenTracker, List.of(thirdGivenCoordinate, fourthGivenCoordinate, fifthGivenCoordinate))
        );
        assertEquals(expectedCapturedTracks, actualCapturedTracks);

        verify(mockedSavedDataProducer, times(givenSavedData.size())).send(dataArgumentCaptor.capture());
        final List<Data> actualCapturedData = dataArgumentCaptor.getAllValues();
        assertEquals(givenSavedData, actualCapturedData);
    }

    private static ConsumingContext createContext(final Coordinate coordinate) {
        final ConsumingContext context = mock(ConsumingContext.class);
        when(context.getCoordinate()).thenReturn(coordinate);
        return context;
    }

    @SuppressWarnings("SameParameterValue")
    private static ConsumingContext createContext(final LocalDateTime dateTime,
                                                  final Coordinate coordinate,
                                                  final double speed,
                                                  final int course,
                                                  final int altitude,
                                                  final int amountOfSatellites,
                                                  final double hdop,
                                                  final int inputs,
                                                  final int outputs,
                                                  final double[] analogInputs,
                                                  final String driverKeyCode,
                                                  final Map<String, Parameter> parametersByNames,
                                                  final Tracker tracker,
                                                  final Address address) {
        final ConsumingContext context = createContext(coordinate);
        when(context.getDateTime()).thenReturn(dateTime);
        when(context.getSpeed()).thenReturn(speed);
        when(context.getCourse()).thenReturn(course);
        when(context.getAltitude()).thenReturn(altitude);
        when(context.getAmountOfSatellites()).thenReturn(amountOfSatellites);
        when(context.getHdop()).thenReturn(hdop);
        when(context.getInputs()).thenReturn(inputs);
        when(context.getOutputs()).thenReturn(outputs);
        when(context.getAnalogInputs()).thenReturn(analogInputs);
        when(context.getDriverKeyCode()).thenReturn(driverKeyCode);
        when(context.getParametersByNames()).thenReturn(parametersByNames);
        when(context.getTracker()).thenReturn(tracker);
        when(context.getAddress()).thenReturn(address);
        return context;
    }

    private static Parameter createParameter(final Long id) {
        return Parameter.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Parameter createParameter(final String name, final Type type, final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static Data createData(final Coordinate coordinate, final Tracker tracker) {
        return Data.builder()
                .coordinate(coordinate)
                .tracker(tracker)
                .build();
    }

    private static Data createData(final Long id, final Coordinate coordinate, final Tracker tracker) {
        return Data.builder()
                .id(id)
                .coordinate(coordinate)
                .tracker(tracker)
                .build();
    }
}
