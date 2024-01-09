package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.ReceivedData;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler.ReceivedDataBuilder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.Value;
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
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class DataPackageHandlerTest {
    private static final int GIVEN_DEFAULT_COURSE = 1005;
    private static final double GIVEN_DEFAULT_SPEED = 1006;
    private static final int GIVEN_DEFAULT_ALTITUDE = 1000;
    private static final int GIVEN_DEFAULT_AMOUNT_OF_SATELLITES = 1001;
    private static final double GIVEN_DEFAULT_REDUCTION_PRECISION = 1002;
    private static final int GIVEN_DEFAULT_INPUTS = 1003;
    private static final int GIVEN_DEFAULT_OUTPUTS = 1004;
    private static final String GIVEN_DEFAULT_DRIVER_KEY_CODE = "driver key code";

    @Mock
    private DataDefaultPropertyConfiguration mockedDataDefaultPropertyConfiguration;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private ReceivedDataValidator mockedReceivedDataValidator;

    @Mock
    private KafkaInboundDataProducer mockedKafkaInboundDataProducer;

    private TestDataPackageHandler dataPackageHandler;

    @Captor
    private ArgumentCaptor<Data> dataArgumentCaptor;

    @Before
    public void initializeDataPackageHandler() {
        dataPackageHandler = new TestDataPackageHandler(
                mockedDataDefaultPropertyConfiguration,
                mockedContextAttributeManager,
                mockedReceivedDataValidator,
                mockedKafkaInboundDataProducer
        );
    }

    @Before
    public void injectGivenDefaultDataProperties() {
        when(mockedDataDefaultPropertyConfiguration.getCourse())
                .thenReturn(GIVEN_DEFAULT_COURSE);
        when(mockedDataDefaultPropertyConfiguration.getSpeed())
                .thenReturn(GIVEN_DEFAULT_SPEED);
        when(mockedDataDefaultPropertyConfiguration.getAltitude())
                .thenReturn(GIVEN_DEFAULT_ALTITUDE);
        when(mockedDataDefaultPropertyConfiguration.getAmountOfSatellites())
                .thenReturn(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES);
        when(mockedDataDefaultPropertyConfiguration.getReductionPrecision())
                .thenReturn(GIVEN_DEFAULT_REDUCTION_PRECISION);
        when(mockedDataDefaultPropertyConfiguration.getInputs())
                .thenReturn(GIVEN_DEFAULT_INPUTS);
        when(mockedDataDefaultPropertyConfiguration.getOutputs())
                .thenReturn(GIVEN_DEFAULT_OUTPUTS);
        when(mockedDataDefaultPropertyConfiguration.getDriverKeyCode())
                .thenReturn(GIVEN_DEFAULT_DRIVER_KEY_CODE);
    }

    @Test
    public void packageShouldBeHandledInCaseExistingLastData() {
        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);

        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);

        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);

        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);

        final List<TestSource> givenSources = List.of(
                firstGivenSource,
                secondGivenSource,
                thirdGivenSource,
                fourthGivenSource
        );
        final TestDataPackage givenRequestPackage = new TestDataPackage(givenSources);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = createTracker(255L);
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        final Data givenLastData = createData(
                256L,
                LocalDateTime.of(2023, 1, 8, 14, 15, 14)
        );
        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.of(givenLastData));

        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
                firstGivenSourceDateTime,
                firstGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);

        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
                secondGivenSourceDateTime,
                secondGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);

        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
                thirdGivenSourceDateTime,
                thirdGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);

        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
                fourthGivenSourceDateTime,
                fourthGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);

        dataPackageHandler.handleConcretePackage(givenRequestPackage, givenContext);

        verify(mockedKafkaInboundDataProducer, times(2)).send(dataArgumentCaptor.capture());
        final List<Data> actualSentData = dataArgumentCaptor.getAllValues();
        final List<Data> expectedSentData = List.of(
                createDataWithDefaultProperties(secondGivenSourceDateTime, secondGivenSourceCoordinate, givenTracker),
                createDataWithDefaultProperties(firstGivenSourceDateTime, firstGivenSourceCoordinate, givenTracker)
        );
        assertEquals(expectedSentData, actualSentData);
    }

    @Test
    public void packageShouldBeHandledInCaseNotExistingLastData() {
        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);

        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);

        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);

        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);

        final List<TestSource> givenSources = List.of(
                firstGivenSource,
                secondGivenSource,
                thirdGivenSource,
                fourthGivenSource
        );
        final TestDataPackage givenRequestPackage = new TestDataPackage(givenSources);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = createTracker(256L);
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(empty());

        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
                firstGivenSourceDateTime,
                firstGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);

        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
                secondGivenSourceDateTime,
                secondGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);

        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
                thirdGivenSourceDateTime,
                thirdGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);

        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
                fourthGivenSourceDateTime,
                fourthGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);

        dataPackageHandler.handleConcretePackage(givenRequestPackage, givenContext);

        verify(mockedKafkaInboundDataProducer, times(3)).send(dataArgumentCaptor.capture());
        final List<Data> actualSentData = dataArgumentCaptor.getAllValues();
        final List<Data> expectedSentData = List.of(
                createDataWithDefaultProperties(thirdGivenSourceDateTime, thirdGivenSourceCoordinate, givenTracker),
                createDataWithDefaultProperties(secondGivenSourceDateTime, secondGivenSourceCoordinate, givenTracker),
                createDataWithDefaultProperties(firstGivenSourceDateTime, firstGivenSourceCoordinate, givenTracker)
        );
        assertEquals(expectedSentData, actualSentData);
    }

    @Test
    public void packageWithoutValidDataShouldBeHandled() {
        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);

        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);

        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);

        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);

        final List<TestSource> givenSources = List.of(
                firstGivenSource,
                secondGivenSource,
                thirdGivenSource,
                fourthGivenSource
        );
        final TestDataPackage givenRequestPackage = new TestDataPackage(givenSources);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = createTracker(256L);
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(empty());

        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
                firstGivenSourceDateTime,
                firstGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(false);

        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
                secondGivenSourceDateTime,
                secondGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(false);

        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
                thirdGivenSourceDateTime,
                thirdGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(false);

        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
                fourthGivenSourceDateTime,
                fourthGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);

        dataPackageHandler.handleConcretePackage(givenRequestPackage, givenContext);

        verifyNoInteractions(mockedKafkaInboundDataProducer);
    }

    @Test
    public void packageWithOldDataShouldBeHandled() {
        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);

        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);

        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);

        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);

        final List<TestSource> givenSources = List.of(
                firstGivenSource,
                secondGivenSource,
                thirdGivenSource,
                fourthGivenSource
        );
        final TestDataPackage givenRequestPackage = new TestDataPackage(givenSources);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = createTracker(255L);
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        final Data givenLastData = createData(
                257L,
                LocalDateTime.of(2024, 1, 8, 14, 15, 14)
        );
        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.of(givenLastData));

        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
                firstGivenSourceDateTime,
                firstGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);

        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
                secondGivenSourceDateTime,
                secondGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);

        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
                thirdGivenSourceDateTime,
                thirdGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);

        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
                fourthGivenSourceDateTime,
                fourthGivenSourceCoordinate,
                givenTracker
        );
        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(true);

        dataPackageHandler.handleConcretePackage(givenRequestPackage, givenContext);

        verifyNoInteractions(mockedKafkaInboundDataProducer);
    }

    @Test
    public void receivedDataBuilderShouldBeCreated() {
        final ReceivedDataBuilder actual = new ReceivedDataBuilder(mockedDataDefaultPropertyConfiguration);

        assertNull(actual.getDateTime());
        assertNull(actual.getCoordinate());
        assertEquals(GIVEN_DEFAULT_COURSE, actual.getCourse());
        assertEquals(GIVEN_DEFAULT_ALTITUDE, actual.getAltitude());
        assertEquals(GIVEN_DEFAULT_SPEED, actual.getSpeed(), 0.);
        assertEquals(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES, actual.getAmountOfSatellites());
        assertEquals(GIVEN_DEFAULT_REDUCTION_PRECISION, actual.getReductionPrecision(), 0.);
        assertEquals(GIVEN_DEFAULT_INPUTS, actual.getInputs());
        assertEquals(GIVEN_DEFAULT_OUTPUTS, actual.getOutputs());
        assertArrayEquals(new double[]{}, actual.getAnalogInputs(), 0.);
        assertEquals(GIVEN_DEFAULT_DRIVER_KEY_CODE, actual.getDriverKeyCode());
        assertTrue(actual.getParametersByNames().isEmpty());
    }

    @Test
    public void parameterShouldBeAdded() {
        final ReceivedDataBuilder givenBuilder = new ReceivedDataBuilder(mockedDataDefaultPropertyConfiguration);

        final String givenParameterName = "name";
        final Parameter givenParameter = createParameter(255L, givenParameterName);

        givenBuilder.addParameter(givenParameter);

        final Map<String, Parameter> actual = givenBuilder.getParametersByNames();
        final Map<String, Parameter> expected = Map.of(givenParameterName, givenParameter);
        assertEquals(expected, actual);
    }

    @Test
    public void receivedDataShouldBeBuilt() {
        final ReceivedDataBuilder givenBuilder = new ReceivedDataBuilder(mockedDataDefaultPropertyConfiguration);

        final LocalDateTime givenDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        givenBuilder.setDateTime(givenDateTime);

        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        givenBuilder.setCoordinate(givenCoordinate);

        final Tracker givenTracker = createTracker(255L);

        final ReceivedData actual = givenBuilder.build(givenTracker);
        final ReceivedData expected = createReceivedDataWithDefaultProperties(
                givenDateTime,
                givenCoordinate,
                givenTracker
        );
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void receivedDataShouldNotBeBuiltBecauseOfNoDateTime() {
        final ReceivedDataBuilder givenBuilder = new ReceivedDataBuilder(mockedDataDefaultPropertyConfiguration);

        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);
        givenBuilder.setCoordinate(givenCoordinate);

        final Tracker givenTracker = createTracker(255L);

        givenBuilder.build(givenTracker);
    }

    @Test(expected = IllegalStateException.class)
    public void receivedDataShouldNotBeBuiltBecauseOfNoCoordinate() {
        final ReceivedDataBuilder givenBuilder = new ReceivedDataBuilder(mockedDataDefaultPropertyConfiguration);

        final LocalDateTime givenDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
        givenBuilder.setDateTime(givenDateTime);

        final Tracker givenTracker = createTracker(255L);

        givenBuilder.build(givenTracker);
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static Data createData(final Long id, final LocalDateTime dateTime) {
        return Data.builder()
                .id(id)
                .dateTime(dateTime)
                .build();
    }

    private static ReceivedData createReceivedDataWithDefaultProperties(final LocalDateTime dateTime,
                                                                        final Coordinate coordinate,
                                                                        final Tracker tracker) {
        return ReceivedData.builder()
                .dateTime(dateTime)
                .coordinate(coordinate)
                .course(GIVEN_DEFAULT_COURSE)
                .speed(GIVEN_DEFAULT_SPEED)
                .altitude(GIVEN_DEFAULT_ALTITUDE)
                .amountOfSatellites(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES)
                .reductionPrecision(GIVEN_DEFAULT_REDUCTION_PRECISION)
                .inputs(GIVEN_DEFAULT_INPUTS)
                .outputs(GIVEN_DEFAULT_OUTPUTS)
                .analogInputs(new double[]{})
                .driverKeyCode(GIVEN_DEFAULT_DRIVER_KEY_CODE)
                .parametersByNames(emptyMap())
                .tracker(tracker)
                .build();
    }

    private static Data createDataWithDefaultProperties(final LocalDateTime dateTime,
                                                        final Coordinate coordinate,
                                                        final Tracker tracker) {
        return Data.builder()
                .dateTime(dateTime)
                .coordinate(coordinate)
                .course(GIVEN_DEFAULT_COURSE)
                .speed(GIVEN_DEFAULT_SPEED)
                .altitude(GIVEN_DEFAULT_ALTITUDE)
                .amountOfSatellites(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES)
                .reductionPrecision(GIVEN_DEFAULT_REDUCTION_PRECISION)
                .inputs(GIVEN_DEFAULT_INPUTS)
                .outputs(GIVEN_DEFAULT_OUTPUTS)
                .analogInputs(new double[]{})
                .driverKeyCode(GIVEN_DEFAULT_DRIVER_KEY_CODE)
                .parametersByNames(emptyMap())
                .tracker(tracker)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Parameter createParameter(final Long id, final String name) {
        return Parameter.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Value
    private static class TestSource {
        LocalDateTime dateTime;
        Coordinate coordinate;
    }

    @Value
    private static class TestDataPackage implements Package {
        List<TestSource> sources;
    }

    private static final class TestDataPackageHandler extends DataPackageHandler<TestDataPackage, TestSource> {

        public TestDataPackageHandler(final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                      final ContextAttributeManager contextAttributeManager,
                                      final ReceivedDataValidator receivedDataValidator,
                                      final KafkaInboundDataProducer kafkaInboundDataProducer) {
            super(
                    TestDataPackage.class,
                    dataDefaultPropertyConfiguration,
                    contextAttributeManager,
                    receivedDataValidator,
                    kafkaInboundDataProducer
            );
        }

        @Override
        protected Stream<TestSource> extractSources(final TestDataPackage requestPackage) {
            return requestPackage.sources.stream();
        }

        @Override
        protected void accumulateComponents(final ReceivedDataBuilder builder, final TestSource testSource) {
            builder.setDateTime(testSource.getDateTime());
            builder.setCoordinate(testSource.getCoordinate());
        }
    }
}
