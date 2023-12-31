package by.bsu.wialontransport.kafka.producer.data;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.kafka.producer.data.KafkaDataProducer.ProducingContext;
import by.bsu.wialontransport.kafka.model.view.SavedParameterView;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData;
import org.junit.Test;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class KafkaSavedDataProducerTest {
    private final KafkaSavedDataProducer producer = new KafkaSavedDataProducer(
            null,
            null,
            null,
            null
    );

    @Test
    public void transportableShouldBeCreated() {
        final Long givenDataId = 255L;
        final long givenEpochSeconds = 123456;
        final double givenLatitude = 53.45;
        final double givenLongitude = 27.5454;
        final int givenCourse = 10;
        final double givenSpeed = 40.4;
        final int givenAltitude = 11;
        final int givenAmountOfSatellites = 40;
        final double givenReductionPrecision = 5.5;
        final int givenInputs = 10;
        final int givenOutputs = 20;
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
        final ProducingContext givenContext = createContext(
                givenDataId,
                givenEpochSeconds,
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

        final TransportableSavedData actual = producer.createTransportable(givenContext);
        final TransportableSavedData expected = TransportableSavedData.builder()
                .id(givenDataId)
                .epochSeconds(givenEpochSeconds)
                .latitude(givenLatitude)
                .longitude(givenLongitude)
                .course(givenCourse)
                .speed(givenSpeed)
                .altitude(givenAltitude)
                .amountOfSatellites(givenAmountOfSatellites)
                .reductionPrecision(givenReductionPrecision)
                .inputs(givenInputs)
                .outputs(givenOutputs)
                .serializedAnalogInputs(givenSerializedAnalogInputs)
                .driverKeyCode(givenDriverKeyCode)
                .serializedParameters(givenSerializedParameters)
                .trackerId(givenTrackerId)
                .addressId(givenAddressId)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void parameterViewShouldBeCreated() {
        final String givenName = "parameter-name";
        final Type givenType = INTEGER;
        final String givenValue = "1";
        final Long givenId = 255L;
        final Parameter givenParameter = createParameter(givenId, givenName, givenType, givenValue);

        final SavedParameterView actual = producer.createParameterView(givenParameter);
        final SavedParameterView expected = new SavedParameterView(givenName, givenType, givenValue, givenId);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static ProducingContext createContext(final Long dataId,
                                                  final long epochSeconds,
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
        final ProducingContext context = mock(ProducingContext.class);
        when(context.getDataId()).thenReturn(dataId);
        when(context.getEpochSeconds()).thenReturn(epochSeconds);
        when(context.getLatitude()).thenReturn(latitude);
        when(context.getLongitude()).thenReturn(longitude);
        when(context.getCourse()).thenReturn(course);
        when(context.getSpeed()).thenReturn(speed);
        when(context.getAltitude()).thenReturn(altitude);
        when(context.getAmountOfSatellites()).thenReturn(amountOfSatellites);
        when(context.getReductionPrecision()).thenReturn(reductionPrecision);
        when(context.getInputs()).thenReturn(inputs);
        when(context.getOutputs()).thenReturn(outputs);
        when(context.getSerializedAnalogInputs()).thenReturn(serializedAnalogInputs);
        when(context.getDriverKeyCode()).thenReturn(driverKeyCode);
        when(context.getSerializedParameters()).thenReturn(serializedParameters);
        when(context.getTrackerId()).thenReturn(trackerId);
        when(context.getAddressId()).thenReturn(addressId);
        return context;
    }

    @SuppressWarnings("SameParameterValue")
    private static Parameter createParameter(final Long id, final String name, final Type type, final String value) {
        return Parameter.builder()
                .id(id)
                .name(name)
                .type(type)
                .value(value)
                .build();
    }
}
