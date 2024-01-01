package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.data.KafkaDataConsumer.ConsumingException;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableData;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData;
import by.bsu.wialontransport.kafka.model.view.ParameterView;
import by.bsu.wialontransport.model.Coordinate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class KafkaDataConsumerTest extends AbstractContextTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private AddressService mockedAddressService;

    private TestKafkaDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        consumer = new TestKafkaDataConsumer(objectMapper, mockedTrackerService, mockedAddressService);
    }

    @Test
    public void genericRecordShouldBeMappedToSource() {
        final Long givenDataId = 255L;
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                12,
                30,
                2,
                43,
                15
        );
        final double givenLatitude = 53.3434;
        final double givenLongitude = 27.4343;
        final int givenCourse = 40;
        final int givenSpeed = 60;
        final int givenAltitude = 10;
        final int givenAmountOfSatellites = 20;
        final double givenReductionPrecision = 40.4;
        final int givenInputs = 15;
        final int givenOutputs = 16;
        final String givenSerializedAnalogInputs = "[0.2,0.3,0.4]";
        final String givenDriverKeyCode = "driver key code";
        final String givenSerializedParameters = """
                [
                  {
                    "name": "first-param",
                    "type": "INTEGER",
                    "value": "1",
                    "id": 256
                  },
                  {
                    "name": "second-param",
                    "type": "STRING",
                    "value": "string",
                    "id": 257
                  }
                ]""";
        final Long givenTrackerId = 256L;
        final Long givenAddressId = 257L;
        final GenericRecord givenGenericRecord = createGenericRecord(
                givenDataId,
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenCourse,
                givenSpeed,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenSerializedAnalogInputs,
                givenDriverKeyCode,
                givenSerializedParameters,
                givenTrackerId,
                givenAddressId
        );

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(mockedTrackerService.findById(same(givenTrackerId))).thenReturn(Optional.of(givenTracker));

        final Address givenAddress = createAddress(givenAddressId);
        when(mockedAddressService.findById(same(givenAddressId))).thenReturn(Optional.of(givenAddress));

        final Data actual = consumer.mapToSource(givenGenericRecord);
        final Data expected = Data.builder()
                .id(givenDataId)
                .dateTime(givenDateTime)
                .coordinate(new Coordinate(givenLatitude, givenLongitude))
                .course(givenCourse)
                .speed(givenSpeed)
                .altitude(givenAltitude)
                .amountOfSatellites(givenAmountOfSatellites)
                .reductionPrecision(givenReductionPrecision)
                .inputs(givenInputs)
                .outputs(givenOutputs)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode(givenDriverKeyCode)
                .parametersByNames(
                        Map.of(
                                "first-param", createParameter("first-param", INTEGER, "1", 256L),
                                "second-param", createParameter("second-param", STRING, "string", 257L)
                        )
                )
                .tracker(givenTracker)
                .address(givenAddress)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = ConsumingException.class)
    public void genericRecordShouldNotBeMappedToSourceBecauseOfThereIsNoTrackerWithGivenId() {
        final Long givenDataId = 256L;
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                12,
                30,
                2,
                43,
                16
        );
        final double givenLatitude = 53.3435;
        final double givenLongitude = 27.4344;
        final int givenCourse = 41;
        final int givenSpeed = 61;
        final int givenAltitude = 11;
        final int givenAmountOfSatellites = 21;
        final double givenReductionPrecision = 40.5;
        final int givenInputs = 16;
        final int givenOutputs = 17;
        final String givenSerializedAnalogInputs = "[0.2,0.3,0.5]";
        final String givenDriverKeyCode = "code";
        final String givenSerializedParameters = """
                [
                  {
                    "name": "first-param",
                    "type": "INTEGER",
                    "value": "1",
                    "id": 258
                  },
                  {
                    "name": "second-param",
                    "type": "STRING",
                    "value": "string",
                    "id": 259
                  }
                ]""";
        final Long givenTrackerId = 260L;
        final Long givenAddressId = 261L;
        final GenericRecord givenGenericRecord = createGenericRecord(
                givenDataId,
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenCourse,
                givenSpeed,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenSerializedAnalogInputs,
                givenDriverKeyCode,
                givenSerializedParameters,
                givenTrackerId,
                givenAddressId
        );

        when(mockedTrackerService.findById(same(givenTrackerId))).thenReturn(empty());

        consumer.mapToSource(givenGenericRecord);
    }

    @Test(expected = ConsumingException.class)
    public void genericRecordShouldNotBeMappedToSourceBecauseAddressWithGivenIdNotExist() {
        final Long givenDataId = 255L;
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                12,
                30,
                2,
                43,
                15
        );
        final double givenLatitude = 53.3434;
        final double givenLongitude = 27.4343;
        final int givenCourse = 40;
        final int givenSpeed = 60;
        final int givenAltitude = 10;
        final int givenAmountOfSatellites = 20;
        final double givenReductionPrecision = 40.4;
        final int givenInputs = 15;
        final int givenOutputs = 16;
        final String givenSerializedAnalogInputs = "[0.2,0.3,0.4]";
        final String givenDriverKeyCode = "driver key code";
        final String givenSerializedParameters = """
                [
                  {
                    "name": "first-param",
                    "type": "INTEGER",
                    "value": "1",
                    "id": 256
                  },
                  {
                    "name": "second-param",
                    "type": "STRING",
                    "value": "string",
                    "id": 257
                  }
                ]""";
        final Long givenTrackerId = 256L;
        final Long givenAddressId = 257L;
        final GenericRecord givenGenericRecord = createGenericRecord(
                givenDataId,
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenCourse,
                givenSpeed,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenSerializedAnalogInputs,
                givenDriverKeyCode,
                givenSerializedParameters,
                givenTrackerId,
                givenAddressId
        );

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(mockedTrackerService.findById(same(givenTrackerId))).thenReturn(Optional.of(givenTracker));

        when(mockedAddressService.findById(same(givenAddressId))).thenReturn(empty());

        consumer.mapToSource(givenGenericRecord);
    }

    @Test(expected = ConsumingException.class)
    public void genericRecordShouldNotBeMappedToSourceBecauseOfThereIsNoDataId() {
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                12,
                30,
                2,
                43,
                15
        );
        final double givenLatitude = 53.3434;
        final double givenLongitude = 27.4343;
        final int givenCourse = 40;
        final int givenSpeed = 60;
        final int givenAltitude = 10;
        final int givenAmountOfSatellites = 20;
        final double givenReductionPrecision = 40.4;
        final int givenInputs = 15;
        final int givenOutputs = 16;
        final String givenSerializedAnalogInputs = "[0.2,0.3,0.4]";
        final String givenDriverKeyCode = "driver key code";
        final String givenSerializedParameters = """
                [
                  {
                    "name": "first-param",
                    "type": "INTEGER",
                    "value": "1",
                    "id": 256
                  },
                  {
                    "name": "second-param",
                    "type": "STRING",
                    "value": "string",
                    "id": 257
                  }
                ]""";
        final Long givenTrackerId = 256L;
        final Long givenAddressId = 257L;
        final GenericRecord givenGenericRecord = createGenericRecord(
                null,
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenCourse,
                givenSpeed,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenSerializedAnalogInputs,
                givenDriverKeyCode,
                givenSerializedParameters,
                givenTrackerId,
                givenAddressId
        );

        consumer.mapToSource(givenGenericRecord);
    }

    @Test(expected = ConsumingException.class)
    public void genericRecordShouldNotBeMappedToSourceBecauseOfThereIsNoAddressId() {
        final Long givenDataId = 255L;
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023,
                12,
                30,
                2,
                43,
                15
        );
        final double givenLatitude = 53.3434;
        final double givenLongitude = 27.4343;
        final int givenCourse = 40;
        final int givenSpeed = 60;
        final int givenAltitude = 10;
        final int givenAmountOfSatellites = 20;
        final double givenReductionPrecision = 40.4;
        final int givenInputs = 15;
        final int givenOutputs = 16;
        final String givenSerializedAnalogInputs = "[0.2,0.3,0.4]";
        final String givenDriverKeyCode = "driver key code";
        final String givenSerializedParameters = """
                [
                  {
                    "name": "first-param",
                    "type": "INTEGER",
                    "value": "1",
                    "id": 256
                  },
                  {
                    "name": "second-param",
                    "type": "STRING",
                    "value": "string",
                    "id": 257
                  }
                ]""";
        final Long givenTrackerId = 256L;
        final GenericRecord givenGenericRecord = createGenericRecord(
                givenDataId,
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenCourse,
                givenSpeed,
                givenAltitude,
                givenAmountOfSatellites,
                givenReductionPrecision,
                givenInputs,
                givenOutputs,
                givenSerializedAnalogInputs,
                givenDriverKeyCode,
                givenSerializedParameters,
                givenTrackerId,
                null
        );

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(mockedTrackerService.findById(same(givenTrackerId))).thenReturn(Optional.of(givenTracker));

        consumer.mapToSource(givenGenericRecord);
    }

    private static GenericRecord createGenericRecord(final Long dataId,
                                                     final LocalDateTime dateTime,
                                                     final double latitude,
                                                     final double longitude,
                                                     final int course,
                                                     final double speed,
                                                     final int altitude,
                                                     final int amountOfSatellites,
                                                     final double reductionPrecision,
                                                     final int inputs,
                                                     final int outputs,
                                                     final String serializedAnalogInputs,
                                                     final String driverKeyCode,
                                                     final String serializedParameters,
                                                     final Long trackerId,
                                                     final Long addressId) {
        final GenericRecord record = mock(GenericRecord.class);
        when(record.get(same(TransportableSavedData.Fields.id))).thenReturn(dataId);
        when(record.get(same(TransportableData.Fields.epochSeconds))).thenReturn(dateTime.toEpochSecond(UTC));
        when(record.get(same(TransportableData.Fields.latitude))).thenReturn(latitude);
        when(record.get(same(TransportableData.Fields.longitude))).thenReturn(longitude);
        when(record.get(same(TransportableData.Fields.course))).thenReturn(course);
        when(record.get(same(TransportableData.Fields.speed))).thenReturn(speed);
        when(record.get(same(TransportableData.Fields.altitude))).thenReturn(altitude);
        when(record.get(same(TransportableData.Fields.amountOfSatellites))).thenReturn(amountOfSatellites);
        when(record.get(same(TransportableData.Fields.reductionPrecision))).thenReturn(reductionPrecision);
        when(record.get(same(TransportableData.Fields.inputs))).thenReturn(inputs);
        when(record.get(same(TransportableData.Fields.outputs))).thenReturn(outputs);
        when(record.get(same(TransportableData.Fields.serializedAnalogInputs))).thenReturn(serializedAnalogInputs);
        when(record.get(same(TransportableData.Fields.driverKeyCode))).thenReturn(driverKeyCode);
        when(record.get(same(TransportableData.Fields.serializedParameters))).thenReturn(serializedParameters);
        when(record.get(same(TransportableData.Fields.trackerId))).thenReturn(trackerId);
        when(record.get(same(TransportableSavedData.Fields.addressId))).thenReturn(addressId);
        return record;
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

    private static Parameter createParameter(final String name, final Type type, final String value, final Long id) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .id(id)
                .build();
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static class TestParameterView extends ParameterView {
        private final Long id;

        @Builder
        @JsonCreator
        public TestParameterView(@JsonProperty("name") final String name,
                                 @JsonProperty("type") final Type type,
                                 @JsonProperty("value") final String value,
                                 @JsonProperty("id") final Long id) {
            super(name, type, value);
            this.id = id;
        }
    }

    private static final class TestKafkaDataConsumer extends KafkaDataConsumer<TestParameterView> {
        private final AddressService addressService;

        public TestKafkaDataConsumer(final ObjectMapper objectMapper,
                                     final TrackerService trackerService,
                                     final AddressService addressService) {
            super(objectMapper, trackerService, TestParameterView.class);
            this.addressService = addressService;
        }

        @Override
        protected void process(final List<Data> data) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Data createData(final ConsumingContext context) {
            return Data.builder()
                    .id(extractDataId(context))
                    .dateTime(context.getDateTime())
                    .coordinate(context.getCoordinate())
                    .course(context.getCourse())
                    .speed(context.getSpeed())
                    .altitude(context.getAltitude())
                    .amountOfSatellites(context.getAmountOfSatellites())
                    .reductionPrecision(context.getReductionPrecision())
                    .inputs(context.getInputs())
                    .outputs(context.getOutputs())
                    .analogInputs(context.getAnalogInputs())
                    .driverKeyCode(context.getDriverKeyCode())
                    .parametersByNames(context.getParametersByNames())
                    .tracker(context.getTracker())
                    .address(context.getAddress())
                    .build();
        }

        @Override
        protected Parameter createParameter(final TestParameterView view) {
            return Parameter.builder()
                    .name(view.getName())
                    .type(view.getType())
                    .value(view.getValue())
                    .id(view.getId())
                    .build();
        }

        @Override
        protected Optional<Address> findSavedAddress(final ConsumingContext context) {
            return addressService.findById(extractAddressId(context));
        }

        private static Long extractDataId(final ConsumingContext context) {
            return extractProperty(context, ConsumingContext::getDataId);
        }

        private static Long extractAddressId(final ConsumingContext context) {
            return extractProperty(context, ConsumingContext::getAddressId);
        }

        private static <T> T extractProperty(final ConsumingContext context,
                                             final Function<ConsumingContext, Optional<T>> getter) {
            return getter.apply(context).orElseThrow(ConsumingException::new);
        }
    }
}
