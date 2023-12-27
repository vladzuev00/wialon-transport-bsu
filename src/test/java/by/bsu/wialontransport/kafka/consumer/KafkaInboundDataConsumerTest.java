//package by.bsu.wialontransport.kafka.consumer;
//
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.crud.service.AddressService;
//import by.bsu.wialontransport.crud.service.DataService;
//import by.bsu.wialontransport.crud.service.TrackerService;
//import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
//import by.bsu.wialontransport.kafka.producer.KafkaSavedDataProducer;
//import by.bsu.wialontransport.kafka.transportable.data.TransportableData;
//import by.bsu.wialontransport.service.geocoding.GeocodingService;
//import org.apache.avro.generic.GenericRecord;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
//import static by.bsu.wialontransport.kafka.transportable.data.TransportableData.Fields.*;
//import static java.time.ZoneOffset.UTC;
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class KafkaInboundDataConsumerTest {
//
//    @Mock
//    private TrackerService mockedTrackerService;
//
//    @Mock
//    private DataService mockedDataService;
//
//    @Mock
//    private AddressService mockedAddressService;
//
//    @Mock
//    private GeocodingService mockedGeocodingService;
//
//    @Mock
//    private KafkaSavedDataProducer mockedKafkaSavedDataProducer;
//
//    @Captor
//    private ArgumentCaptor<List<Data>> listOfDataArgumentCaptor;
//
//    @Captor
//    private ArgumentCaptor<Data> dataArgumentCaptor;
//
//    @Captor
//    private ArgumentCaptor<Address> addressArgumentCaptor;
//
//    private AbstractKafkaDataConsumer consumer;
//
//    @Before
//    public void initializeConsumer() {
//        this.consumer = new KafkaInboundDataConsumer(
//                this.mockedTrackerService,
//                this.mockedDataService,
//                this.mockedAddressService,
//                this.mockedGeocodingService,
//                this.mockedKafkaSavedDataProducer
//        );
//    }
//
//    @Test
//    public void genericRecordShouldBeMappedToData() {
//        final LocalDate givenDate = LocalDate.of(2023, 5, 18);
//        final LocalTime givenTime = LocalTime.of(17, 37, 20);
//        final Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
//        final Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
//        final int givenSpeed = 7;
//        final int givenCourse = 8;
//        final int givenAltitude = 9;
//        final int givenAmountOfSatellites = 10;
//        final double givenReductionPrecision = 0.11;
//        final int givenInputs = 12;
//        final int givenOutputs = 13;
//        final double[] givenAnalogInputs = new double[]{14.4, 15.5, 16.6};
//        final String givenDriverKeyCode = "driverKeyCode";
//        final Map<String, Parameter> givenParametersByNames = Map.of(
//                "first", createParameter("first", DOUBLE, "4.4"),
//                "second", createParameter("second", INTEGER, "4")
//        );
//        final Long givenTrackerId = 256L;
//
//        final GenericRecord givenGenericRecord = createGenericRecord(
//                givenDate,
//                givenTime,
//                givenLatitude,
//                givenLongitude,
//                givenSpeed,
//                givenCourse,
//                givenAltitude,
//                givenAmountOfSatellites,
//                givenReductionPrecision,
//                givenInputs,
//                givenOutputs,
//                "14.4,15.5,16.6",
//                givenDriverKeyCode,
//                "first:2:4.4,second:1:4",
//                givenTrackerId
//        );
//
//        final Tracker givenTracker = createTracker(givenTrackerId);
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));
//
//        final Address givenAddress = createAddress(257L);
//        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude)).thenReturn(
//                Optional.of(givenAddress)
//        );
//
//        final Data actual = this.consumer.mapToData(givenGenericRecord);
//        final Data expected = Data.builder()
//                .date(givenDate)
//                .time(givenTime)
//                .latitude(givenLatitude)
//                .longitude(givenLongitude)
//                .speed(givenSpeed)
//                .course(givenCourse)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .reductionPrecision(givenReductionPrecision)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .analogInputs(givenAnalogInputs)
//                .driverKeyCode(givenDriverKeyCode)
//                .parametersByNames(givenParametersByNames)
//                .tracker(givenTracker)
//                .address(givenAddress)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test(expected = DataConsumingException.class)
//    public void genericRecordShouldNotBeMappedToDateBecauseOfAddressWasNotReceived() {
//        final LocalDate givenDate = LocalDate.of(2023, 5, 18);
//        final LocalTime givenTime = LocalTime.of(17, 37, 20);
//        final Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
//        final Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
//        final int givenSpeed = 7;
//        final int givenCourse = 8;
//        final int givenAltitude = 9;
//        final int givenAmountOfSatellites = 10;
//        final double givenReductionPrecision = 0.11;
//        final int givenInputs = 12;
//        final int givenOutputs = 13;
//        final String givenDriverKeyCode = "driverKeyCode";
//        final Long givenTrackerId = 256L;
//
//        final GenericRecord givenGenericRecord = createGenericRecord(
//                givenDate,
//                givenTime,
//                givenLatitude,
//                givenLongitude,
//                givenSpeed,
//                givenCourse,
//                givenAltitude,
//                givenAmountOfSatellites,
//                givenReductionPrecision,
//                givenInputs,
//                givenOutputs,
//                "14.4,15.5,16.6",
//                givenDriverKeyCode,
//                "first:2:4.4,second:1:4",
//                givenTrackerId
//        );
//
//        final Tracker givenTracker = createTracker(givenTrackerId);
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));
//
//        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude)).thenReturn(empty());
//
//        this.consumer.mapToData(givenGenericRecord);
//    }
//
//    @Test
//    public void dataShouldBeProcessed() {
//        final List<Data> givenData = List.of(
//                createDataWithAddress(258L),
//                createDataWithAddress(258L),
//                createDataWithAddress(259L),
//                //address's id - 260
//                createDataWithAddress(createAddressWithCityName("first-city")),
//                //address's id - 260
//                createDataWithAddress(createAddressWithCityName("first-city")),
//                //address's id - 261
//                createDataWithAddress(createAddressWithCityName("second-name"))
//        );
//
//        final Address firstGivenNewSavedAddress = createAddress(260L);
//        final Address secondGivenNewSavedAddress = createAddress(261L);
//        when(this.mockedAddressService.save(any(Address.class)))
//                .thenReturn(firstGivenNewSavedAddress)
//                .thenReturn(secondGivenNewSavedAddress);
//
//        final List<Data> expectedNotSavedDataWithSavedAddress = List.of(
//                createDataWithAddress(258L),
//                createDataWithAddress(258L),
//                createDataWithAddress(259L),
//                createDataWithAddress(260L),
//                createDataWithAddress(260L),
//                createDataWithAddress(261L)
//        );
//        final List<Data> givenSavedData = List.of(
//                createDataWithAddress(300L, 258L),
//                createDataWithAddress(301L, 258L),
//                createDataWithAddress(302L, 259L),
//                createDataWithAddress(303L, 260L),
//                createDataWithAddress(304L, 260L),
//                createDataWithAddress(305L, 261L)
//        );
//        when(this.mockedDataService.saveAll(anyList())).thenReturn(givenSavedData);
//
//        this.consumer.processData(givenData);
//
//        verify(this.mockedAddressService, times(2))
//                .save(this.addressArgumentCaptor.capture());
//        verify(this.mockedDataService, times(1))
//                .saveAll(this.listOfDataArgumentCaptor.capture());
//        verify(this.mockedKafkaSavedDataProducer, times(givenSavedData.size()))
//                .send(this.dataArgumentCaptor.capture());
//
//        assertTrue(areAddressesNew(this.addressArgumentCaptor.getAllValues()));
//        assertEquals(expectedNotSavedDataWithSavedAddress, this.listOfDataArgumentCaptor.getValue());
//        assertEquals(givenSavedData, this.dataArgumentCaptor.getAllValues());
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
//                                           final DataEntity.Latitude.Type type) {
//        return Latitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
//                                             final DataEntity.Longitude.Type type) {
//        return Longitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static Tracker createTracker(final Long id) {
//        return Tracker.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Parameter createParameter(final String name, final ParameterEntity.Type type, final String value) {
//        return Parameter.builder()
//                .name(name)
//                .type(type)
//                .value(value)
//                .build();
//    }
//
//    private static Data createDataWithAddress(final Long addressId) {
//        return createDataWithAddress(null, addressId);
//    }
//
//    private static Data createDataWithAddress(final Long id, final Long addressId) {
//        return Data.builder()
//                .id(id)
//                .address(createAddress(addressId))
//                .build();
//    }
//
//    private static Data createDataWithAddress(final Address address) {
//        return Data.builder()
//                .address(address)
//                .build();
//    }
//
//    private static Address createAddress(final Long id) {
//        return Address.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Address createAddressWithCityName(final String cityName) {
//        return Address.builder()
//                .cityName(cityName)
//                .build();
//    }
//
//    private static boolean areAddressesNew(final List<Address> addresses) {
//        return addresses.stream()
//                .allMatch(KafkaInboundDataConsumerTest::isAddressNew);
//    }
//
//    private static boolean isAddressNew(final Address address) {
//        return address.getId() == null;
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static GenericRecord createGenericRecord(final LocalDate date,
//                                                     final LocalTime time,
//                                                     final Latitude latitude,
//                                                     final Longitude longitude,
//                                                     final int speed,
//                                                     final int course,
//                                                     final int altitude,
//                                                     final int amountOfSatellites,
//                                                     final double reductionPrecision,
//                                                     final int inputs,
//                                                     final int outputs,
//                                                     final String serializedAnalogInputs,
//                                                     final String driverKeyCode,
//                                                     final String serializedParameters,
//                                                     final Long trackerId) {
//        final GenericRecord genericRecord = mock(GenericRecord.class);
//        injectDateTimeInGenericRecord(genericRecord, date, time);
//        injectLatitudeInGenericRecord(genericRecord, latitude);
//        injectLongitudeInGenericRecord(genericRecord, longitude);
//        when(genericRecord.get(TransportableData.Fields.speed)).thenReturn(speed);
//        when(genericRecord.get(TransportableData.Fields.course)).thenReturn(course);
//        when(genericRecord.get(TransportableData.Fields.altitude)).thenReturn(altitude);
//        when(genericRecord.get(TransportableData.Fields.amountOfSatellites)).thenReturn(amountOfSatellites);
//        when(genericRecord.get(TransportableData.Fields.reductionPrecision)).thenReturn(reductionPrecision);
//        when(genericRecord.get(TransportableData.Fields.inputs)).thenReturn(inputs);
//        when(genericRecord.get(TransportableData.Fields.outputs)).thenReturn(outputs);
//        when(genericRecord.get(TransportableData.Fields.serializedAnalogInputs)).thenReturn(serializedAnalogInputs);
//        when(genericRecord.get(TransportableData.Fields.driverKeyCode)).thenReturn(driverKeyCode);
//        when(genericRecord.get(TransportableData.Fields.serializedParameters)).thenReturn(serializedParameters);
//        when(genericRecord.get(TransportableData.Fields.trackerId)).thenReturn(trackerId);
//        return genericRecord;
//    }
//
//    private static void injectDateTimeInGenericRecord(final GenericRecord genericRecord,
//                                                      final LocalDate date,
//                                                      final LocalTime time) {
//        final LocalDateTime dateTime = LocalDateTime.of(date, time);
//        final long epochSeconds = dateTime.toEpochSecond(UTC);
//        when(genericRecord.get(TransportableData.Fields.epochSeconds)).thenReturn(epochSeconds);
//    }
//
//    private static void injectLatitudeInGenericRecord(final GenericRecord genericRecord, final Latitude latitude) {
//        when(genericRecord.get(latitudeDegrees)).thenReturn(latitude.getDegrees());
//        when(genericRecord.get(latitudeMinutes)).thenReturn(latitude.getMinutes());
//        when(genericRecord.get(latitudeMinuteShare)).thenReturn(latitude.getMinuteShare());
//        when(genericRecord.get(latitudeTypeValue)).thenReturn((int) latitude.getType().getValue());
//    }
//
//    private static void injectLongitudeInGenericRecord(final GenericRecord genericRecord,
//                                                       final Longitude longitude) {
//        when(genericRecord.get(longitudeDegrees)).thenReturn(longitude.getDegrees());
//        when(genericRecord.get(longitudeMinutes)).thenReturn(longitude.getMinutes());
//        when(genericRecord.get(longitudeMinuteShare)).thenReturn(longitude.getMinuteShare());
//        when(genericRecord.get(longitudeTypeValue)).thenReturn((int) longitude.getType().getValue());
//    }
//}
//
