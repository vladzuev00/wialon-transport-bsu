package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.producer.KafkaSavedDataProducer;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaInboundDataConsumerTest {

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private DataService mockedDataService;

    @Mock
    private AddressService mockedAddressService;

    @Mock
    private GeocodingService mockedGeocodingService;

    @Mock
    private KafkaSavedDataProducer mockedKafkaSavedDataProducer;

    @Captor
    private ArgumentCaptor<List<Data>> listOfDataArgumentCaptor;

    @Captor
    private ArgumentCaptor<Data> dataArgumentCaptor;

    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    private AbstractKafkaDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        this.consumer = new KafkaInboundDataConsumer(
                this.mockedTrackerService,
                this.mockedDataService,
                this.mockedAddressService,
                this.mockedGeocodingService,
                this.mockedKafkaSavedDataProducer
        );
    }

    @Test
    public void dataShouldBeCreated() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.now();
        final LocalTime givenTime = LocalTime.now();
        final Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
        final Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
        final int givenSpeed = 7;
        final int givenCourse = 8;
        final int givenAltitude = 9;
        final int givenAmountOfSatellites = 10;
        final double givenReductionPrecision = 0.11;
        final int givenInputs = 12;
        final int givenOutputs = 13;
        final double[] givenAnalogInputs = new double[]{14.4, 15.5, 16.6};
        final String givenDriverKeyCode = "driverKeyCode";
        final Map<String, Parameter> givenParametersByNames = Map.of(
                "first-parameter", createParameter("first-parameter", DOUBLE, "4.4"),
                "second-parameter", createParameter("second-parameter", INTEGER, "4")
        );
        final Tracker givenTracker = createTracker(256L);
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final Address givenAddress = createAddress(257L);
        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude)).thenReturn(
                Optional.of(givenAddress)
        );

        final Data actual = this.consumer.createData(
                givenId,
                givenDate,
                givenTime,
                givenLatitude,
                givenLongitude,
                givenSpeed,
                givenCourse,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenAnalogInputs,
                givenDriverKeyCode,
                givenParametersByNames,
                givenTracker,
                givenGenericRecord
        );
        final Data expected = new Data(
                givenId,
                givenDate,
                givenTime,
                givenLatitude,
                givenLongitude,
                givenSpeed,
                givenCourse,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenAnalogInputs,
                givenDriverKeyCode,
                givenParametersByNames,
                givenTracker,
                givenAddress
        );
        assertEquals(expected, actual);
    }

    @Test(expected = DataConsumingException.class)
    public void dataShouldNotBeCreatedBecauseOfAddressWasNotFound() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.now();
        final LocalTime givenTime = LocalTime.now();
        final Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
        final Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
        final int givenSpeed = 7;
        final int givenCourse = 8;
        final int givenAltitude = 9;
        final int givenAmountOfSatellites = 10;
        final double givenReductionPrecision = 0.11;
        final int givenInputs = 12;
        final int givenOutputs = 13;
        final double[] givenAnalogInputs = new double[]{14.4, 15.5, 16.6};
        final String givenDriverKeyCode = "driverKeyCode";
        final Map<String, Parameter> givenParametersByNames = Map.of(
                "first-parameter", createParameter("first-parameter", DOUBLE, "4.4"),
                "second-parameter", createParameter("second-parameter", INTEGER, "4")
        );
        final Tracker givenTracker = createTracker(256L);
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude)).thenReturn(empty());

        this.consumer.createData(
                givenId,
                givenDate,
                givenTime,
                givenLatitude,
                givenLongitude,
                givenSpeed,
                givenCourse,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenAnalogInputs,
                givenDriverKeyCode,
                givenParametersByNames,
                givenTracker,
                givenGenericRecord
        );
    }

    @Test
    public void dataShouldBeProcessed() {
        final List<Data> givenData = List.of(
                createDataWithAddress(258L),
                createDataWithAddress(258L),
                createDataWithAddress(259L),
                //address's id - 260
                createDataWithAddress(createAddressWithCityName("first-city")),
                //address's id - 260
                createDataWithAddress(createAddressWithCityName("first-city")),
                //address's id - 261
                createDataWithAddress(createAddressWithCityName("second-name"))
        );

        final Address firstGivenNewSavedAddress = createAddress(260L);
        final Address secondGivenNewSavedAddress = createAddress(261L);
        when(this.mockedAddressService.save(any(Address.class)))
                .thenReturn(firstGivenNewSavedAddress)
                .thenReturn(secondGivenNewSavedAddress);

        final List<Data> expectedNotSavedDataWithSavedAddress = List.of(
                createDataWithAddress(258L),
                createDataWithAddress(258L),
                createDataWithAddress(259L),
                createDataWithAddress(260L),
                createDataWithAddress(260L),
                createDataWithAddress(261L)
        );
        final List<Data> givenSavedData = List.of(
                createDataWithAddress(300L, 258L),
                createDataWithAddress(301L, 258L),
                createDataWithAddress(302L, 259L),
                createDataWithAddress(303L, 260L),
                createDataWithAddress(304L, 260L),
                createDataWithAddress(305L, 261L)
        );
        when(this.mockedDataService.saveAll(anyList())).thenReturn(givenSavedData);

        this.consumer.processData(givenData);

        verify(this.mockedAddressService, times(2))
                .save(this.addressArgumentCaptor.capture());
        verify(this.mockedDataService, times(1))
                .saveAll(this.listOfDataArgumentCaptor.capture());
        verify(this.mockedKafkaSavedDataProducer, times(givenSavedData.size()))
                .send(this.dataArgumentCaptor.capture());

        assertTrue(areAddressesNew(this.addressArgumentCaptor.getAllValues()));
        assertEquals(expectedNotSavedDataWithSavedAddress, this.listOfDataArgumentCaptor.getValue());
        assertEquals(givenSavedData, this.dataArgumentCaptor.getAllValues());
    }

    private static Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
                                           final DataEntity.Latitude.Type type) {
        return Latitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
                                             final DataEntity.Longitude.Type type) {
        return Longitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static Parameter createParameter(final String name, final ParameterEntity.Type type, final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    private static Data createDataWithAddress(final Long addressId) {
        return createDataWithAddress(null, addressId);
    }

    private static Data createDataWithAddress(final Long id, final Long addressId) {
        return Data.builder()
                .id(id)
                .address(createAddress(addressId))
                .build();
    }

    private static Data createDataWithAddress(final Address address) {
        return Data.builder()
                .address(address)
                .build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static Address createAddressWithCityName(final String cityName) {
        return Address.builder()
                .cityName(cityName)
                .build();
    }

    private static boolean areAddressesNew(final List<Address> addresses) {
        return addresses.stream()
                .allMatch(KafkaInboundDataConsumerTest::isAddressNew);
    }

    private static boolean isAddressNew(final Address address) {
        return address.getId() == null;
    }
}

