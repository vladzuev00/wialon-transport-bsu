package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.kafka.consumer.data.KafkaDataConsumer.ConsumingContext;
import by.bsu.wialontransport.kafka.consumer.data.KafkaDataConsumer.ConsumingException;
import by.bsu.wialontransport.kafka.model.view.SavedParameterView;
import by.bsu.wialontransport.model.Coordinate;
import nl.altindag.log.LogCaptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaSavedDataConsumerTest {

    private final LogCaptor logCaptor = forClass(KafkaSavedDataConsumer.class);

    @Mock
    private AddressService mockedAddressService;

    private KafkaSavedDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        consumer = new KafkaSavedDataConsumer(null, null, mockedAddressService);
    }

    @Test
    public void dataShouldBeCreated() {
        final LocalDateTime givenDateTime = LocalDateTime.of(2024, 1, 1, 3, 1, 1);
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        final double givenSpeed = 40.4;
        final int givenCourse = 4;
        final int givenAltitude = 5;
        final int givenAmountOfSatellites = 10;
        final double givenReductionPrecision = 4.4;
        final int givenInputs = 4;
        final int givenOutputs = 5;
        final double[] givenAnalogInputs = {3, 4.4, 5};
        final String givenDriverKeyCode = "code";
        final Map<String, Parameter> givenParametersByNames = Map.of(
                "first", createParameter(1L),
                "second", createParameter(2L)
        );
        final Tracker givenTracker = createTracker(3L);
        final Long givenAddressId = 4L;
        final Address givenAddress = createAddress(givenAddressId);
        final Long givenDataId = 5L;
        final ConsumingContext givenContext = createContext(
                givenDateTime,
                givenCoordinate,
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
                givenAddress,
                givenDataId,
                givenAddressId
        );

        @SuppressWarnings("unchecked") final Data actual = consumer.createData(givenContext);
        final Data expected = Data.builder()
                .id(givenDataId)
                .dateTime(givenDateTime)
                .coordinate(givenCoordinate)
                .course(givenCourse)
                .speed(givenSpeed)
                .altitude(givenAltitude)
                .amountOfSatellites(givenAmountOfSatellites)
                .reductionPrecision(givenReductionPrecision)
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

    @SuppressWarnings("unchecked")
    @Test(expected = ConsumingException.class)
    public void dataShouldNotBeCreatedBecauseOfNoDataId() {
        final LocalDateTime givenDateTime = LocalDateTime.of(2024, 1, 1, 3, 1, 1);
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        final double givenSpeed = 40.4;
        final int givenCourse = 4;
        final int givenAltitude = 5;
        final int givenAmountOfSatellites = 10;
        final double givenReductionPrecision = 4.4;
        final int givenInputs = 4;
        final int givenOutputs = 5;
        final double[] givenAnalogInputs = {3, 4.4, 5};
        final String givenDriverKeyCode = "code";
        final Map<String, Parameter> givenParametersByNames = Map.of(
                "first", createParameter(1L),
                "second", createParameter(2L)
        );
        final Tracker givenTracker = createTracker(3L);
        final Long givenAddressId = 4L;
        final Address givenAddress = createAddress(givenAddressId);
        final ConsumingContext givenContext = createContext(
                givenDateTime,
                givenCoordinate,
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
                givenAddress,
                null,
                givenAddressId
        );

        consumer.createData(givenContext);
    }

    @Test
    public void parameterShouldBeCreated() {
        final String givenName = "name";
        final Type givenType = INTEGER;
        final String givenValue = "1";
        final Long givenId = 1L;
        final SavedParameterView givenView = new SavedParameterView(givenName, givenType, givenValue, givenId);

        final Parameter actual = consumer.createParameter(givenView);
        final Parameter expected = createParameter(givenName, givenType, givenValue, givenId);
        assertEquals(expected, actual);
    }

    @Test
    public void addressShouldBeFound() {
        final Long givenAddressId = 1L;
        final ConsumingContext givenContext = createContext(givenAddressId);

        final Address givenAddress = createAddress(givenAddressId);
        when(mockedAddressService.findById(same(givenAddressId))).thenReturn(Optional.of(givenAddress));

        @SuppressWarnings("unchecked") final Optional<Address> optionalActual = consumer.findSavedAddress(givenContext);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        assertSame(givenAddress, actual);
    }

    @Test
    public void addressShouldNotBeFound() {
        final Long givenAddressId = 1L;
        final ConsumingContext givenContext = createContext(givenAddressId);

        when(mockedAddressService.findById(same(givenAddressId))).thenReturn(empty());

        @SuppressWarnings("unchecked") final Optional<Address> optionalActual = consumer.findSavedAddress(givenContext);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ConsumingException.class)
    public void addressShouldNotBeFoundBecauseOfNoAddressId() {
        final ConsumingContext givenContext = mock(ConsumingContext.class);

        consumer.findSavedAddress(givenContext);
    }

    @Test
    public void dataShouldBeProcessed() {
        final List<Data> givenData = List.of(
                createData(1L),
                createData(2L)
        );

        consumer.process(givenData);

        final List<String> actualCapturedLogs = logCaptor.getLogs();
        final List<String> expectedCapturedLogs = List.of(
                "Consuming saved data: %s".formatted(givenData)
        );
        assertEquals(expectedCapturedLogs, actualCapturedLogs);
    }


    private static Parameter createParameter(final Long id) {
        return Parameter.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Parameter createParameter(final String givenName,
                                             final Type givenType,
                                             final String givenValue,
                                             final Long id) {
        return Parameter.builder()
                .name(givenName)
                .type(givenType)
                .value(givenValue)
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
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

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    private static ConsumingContext createContext(final Long addressId) {
        final ConsumingContext context = mock(ConsumingContext.class);
        when(context.getAddressId()).thenReturn(ofNullable(addressId));
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
                                                  final Address address,
                                                  final Long dataId,
                                                  final Long addressId) {
        final ConsumingContext context = createContext(addressId);
        when(context.getDateTime()).thenReturn(dateTime);
        when(context.getCoordinate()).thenReturn(coordinate);
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
        when(context.getDataId()).thenReturn(ofNullable(dataId));
        return context;
    }
}
