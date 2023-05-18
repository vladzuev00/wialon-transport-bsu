package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.kafka.transportable.TransportableSavedData.Fields.addressId;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaSavedDataConsumerTest {
    private static final String FIELD_NAME_LOGGER = "log";
    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";

    @Mock
    private Logger mockedLogger;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private AddressService mockedAddressService;

    @Captor
    private ArgumentCaptor<List<Data>> listOfDataArgumentCaptor;

    private KafkaSavedDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        this.consumer = new KafkaSavedDataConsumer(this.mockedTrackerService, this.mockedAddressService);
    }

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
            unsafeField.setAccessible(true);
        }
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

    @Test
    public void dataShouldBeCreated() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.now();
        final LocalTime givenTime = LocalTime.now();
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
                "first-parameter", createParameter("first-parameter", DOUBLE, "4.4"),
                "second-parameter", createParameter("second-parameter", INTEGER, "4")
        );
        final Tracker givenTracker = createTracker(256L);

        final Long givenAddressId = 257L;
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);
        when(givenGenericRecord.get(addressId)).thenReturn(givenAddressId);

        final Address givenAddress = createAddress(givenAddressId);
        when(this.mockedAddressService.findById(givenAddressId)).thenReturn(Optional.of(givenAddress));

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
    public void dataShouldNotBeCreatedBecauseOfAddressWithGivenIdDoesNotExist() {
        final Long givenId = 255L;
        final LocalDate givenDate = LocalDate.now();
        final LocalTime givenTime = LocalTime.now();
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
                "first-parameter", createParameter("first-parameter", DOUBLE, "4.4"),
                "second-parameter", createParameter("second-parameter", INTEGER, "4")
        );
        final Tracker givenTracker = createTracker(256L);

        final Long givenAddressId = 257L;
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);
        when(givenGenericRecord.get(addressId)).thenReturn(givenAddressId);

        when(this.mockedAddressService.findById(givenAddressId)).thenReturn(empty());

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

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
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
}
