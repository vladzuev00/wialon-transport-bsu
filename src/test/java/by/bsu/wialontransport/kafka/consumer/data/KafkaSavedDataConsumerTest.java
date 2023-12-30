package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.consumer.data.KafkaDataConsumer.ConsumingContext;
import by.bsu.wialontransport.model.Coordinate;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class KafkaSavedDataConsumerTest {
    private final KafkaSavedDataConsumer consumer = new KafkaSavedDataConsumer(
            null,
            null,
            null
    );

    @Test
    public void dataShouldBeCreated() {
        throw new RuntimeException();
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
                                                  final double reductionPrecision,
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
        when(context.getReductionPrecision()).thenReturn(reductionPrecision);
        when(context.getInputs()).thenReturn(inputs);
        when(context.getOutputs()).thenReturn(outputs);
        when(context.getAnalogInputs()).thenReturn(analogInputs);
        when(context.getDriverKeyCode()).thenReturn(driverKeyCode);
        when(context.getParametersByNames()).thenReturn(parametersByNames);
        when(context.getTracker()).thenReturn(tracker);
        when(context.getAddress()).thenReturn(address);
        return context;
    }
}
