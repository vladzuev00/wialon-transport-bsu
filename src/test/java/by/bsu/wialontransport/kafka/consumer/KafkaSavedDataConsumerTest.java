package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.*;
import static by.bsu.wialontransport.kafka.transportable.TransportableSavedData.Fields.addressId;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public final class KafkaSavedDataConsumerTest extends AbstractContextTest {
    private static final String FIELD_NAME_LOGGER = "log";
    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";

    @Mock
    private Logger mockedLogger;

    @MockBean
    private TrackerService mockedTrackerService;

    @MockBean
    private AddressService mockedAddressService;

    @Captor
    private ArgumentCaptor<List<Data>> listOfDataArgumentCaptor;

    @Autowired
    private KafkaSavedDataConsumer consumer;

    @Before
    public void injectMockedLogger()
            throws Exception {
        final Field unsafeField = Unsafe.class.getDeclaredField(FIELD_NAME_THE_UNSAFE);
        unsafeField.setAccessible(true);
        try {
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Field ourField = KafkaSavedDataConsumer.class.getDeclaredField(FIELD_NAME_LOGGER);
            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, this.mockedLogger);
        } finally {
            unsafeField.setAccessible(false);
        }
    }

    @Test
    public void genericRecordShouldBeMappedToData() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.of(2023, 5, 18);
        final LocalTime givenTime = LocalTime.of(17, 37, 20);
        final Data.Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
        final Data.Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
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
                "first", createParameter("first", DOUBLE, "4.4"),
                "second", createParameter("second", INTEGER, "4")
        );
        final Long givenTrackerId = 256L;

        final GenericRecord givenGenericRecord = createGenericRecord(
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
                "14.4,15.5,16.6",
                givenDriverKeyCode,
                "first:2:4.4,second:1:4",
                givenTrackerId
        );

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));

        final Long givenAddressId = 257L;
        when(givenGenericRecord.get(addressId)).thenReturn(givenAddressId);

        final Address givenAddress = createAddress(givenAddressId);
        when(this.mockedAddressService.findById(givenAddressId)).thenReturn(Optional.of(givenAddress));

        final Data actual = this.consumer.mapToData(givenGenericRecord);
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
    public void genericRecordShouldNotBeMappedToDataBecauseOfThereIsNotAddressWithGivenId() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.of(2023, 5, 18);
        final LocalTime givenTime = LocalTime.of(17, 37, 20);
        final Data.Latitude givenLatitude = createLatitude(1, 2, 3, SOUTH);
        final Data.Longitude givenLongitude = createLongitude(4, 5, 6, EAST);
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
                "first", createParameter("first", DOUBLE, "4.4"),
                "second", createParameter("second", INTEGER, "4")
        );
        final Long givenTrackerId = 256L;

        final GenericRecord givenGenericRecord = createGenericRecord(
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
                "14.4,15.5,16.6",
                givenDriverKeyCode,
                "first:2:4.4,second:1:4",
                givenTrackerId
        );

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));

        final Long givenAddressId = 257L;
        when(givenGenericRecord.get(addressId)).thenReturn(givenAddressId);

        when(this.mockedAddressService.findById(givenAddressId)).thenReturn(empty());

        this.consumer.mapToData(givenGenericRecord);
    }

    @Test
    public void dataShouldBeProcessed() {
        final List<Data> givenData = List.of(createData(255L), createData(256L));

        this.consumer.processData(givenData);

        verify(this.mockedLogger, times(1)).info(
                eq("Consuming saved data: {}"), this.listOfDataArgumentCaptor.capture()
        );
        assertEquals(givenData, this.listOfDataArgumentCaptor.getValue());
    }

    private static Data.Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
                                                final DataEntity.Latitude.Type type) {
        return Data.Latitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Data.Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
                                                  final DataEntity.Longitude.Type type) {
        return Data.Longitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Parameter createParameter(final String name, final ParameterEntity.Type type, final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static GenericRecord createGenericRecord(final Long id,
                                                     final LocalDate date,
                                                     final LocalTime time,
                                                     final Data.Latitude latitude,
                                                     final Data.Longitude longitude,
                                                     final int speed,
                                                     final int course,
                                                     final int altitude,
                                                     final int amountOfSatellites,
                                                     final double reductionPrecision,
                                                     final int inputs,
                                                     final int outputs,
                                                     final String serializedAnalogInputs,
                                                     final String driverKeyCode,
                                                     final String serializedParameters,
                                                     final Long trackerId) {
        final GenericRecord genericRecord = mock(GenericRecord.class);
        when(genericRecord.get(TransportableData.Fields.id)).thenReturn(id);
        injectDateTimeInGenericRecord(genericRecord, date, time);
        injectLatitudeInGenericRecord(genericRecord, latitude);
        injectLongitudeInGenericRecord(genericRecord, longitude);
        when(genericRecord.get(TransportableData.Fields.speed)).thenReturn(speed);
        when(genericRecord.get(TransportableData.Fields.course)).thenReturn(course);
        when(genericRecord.get(TransportableData.Fields.altitude)).thenReturn(altitude);
        when(genericRecord.get(TransportableData.Fields.amountOfSatellites)).thenReturn(amountOfSatellites);
        when(genericRecord.get(TransportableData.Fields.reductionPrecision)).thenReturn(reductionPrecision);
        when(genericRecord.get(TransportableData.Fields.inputs)).thenReturn(inputs);
        when(genericRecord.get(TransportableData.Fields.outputs)).thenReturn(outputs);
        when(genericRecord.get(TransportableData.Fields.serializedAnalogInputs)).thenReturn(serializedAnalogInputs);
        when(genericRecord.get(TransportableData.Fields.driverKeyCode)).thenReturn(driverKeyCode);
        when(genericRecord.get(TransportableData.Fields.serializedParameters)).thenReturn(serializedParameters);
        when(genericRecord.get(TransportableData.Fields.trackerId)).thenReturn(trackerId);
        return genericRecord;
    }

    private static void injectDateTimeInGenericRecord(final GenericRecord genericRecord,
                                                      final LocalDate date,
                                                      final LocalTime time) {
        final LocalDateTime dateTime = LocalDateTime.of(date, time);
        final long epochSeconds = dateTime.toEpochSecond(UTC);
        when(genericRecord.get(TransportableData.Fields.epochSeconds)).thenReturn(epochSeconds);
    }

    private static void injectLatitudeInGenericRecord(final GenericRecord genericRecord, final Data.Latitude latitude) {
        when(genericRecord.get(latitudeDegrees)).thenReturn(latitude.getDegrees());
        when(genericRecord.get(latitudeMinutes)).thenReturn(latitude.getMinutes());
        when(genericRecord.get(latitudeMinuteShare)).thenReturn(latitude.getMinuteShare());
        when(genericRecord.get(latitudeTypeValue)).thenReturn((int) latitude.getType().getValue());
    }

    private static void injectLongitudeInGenericRecord(final GenericRecord genericRecord,
                                                       final Data.Longitude longitude) {
        when(genericRecord.get(longitudeDegrees)).thenReturn(longitude.getDegrees());
        when(genericRecord.get(longitudeMinutes)).thenReturn(longitude.getMinutes());
        when(genericRecord.get(longitudeMinuteShare)).thenReturn(longitude.getMinuteShare());
        when(genericRecord.get(longitudeTypeValue)).thenReturn((int) longitude.getType().getValue());
    }

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }
}
