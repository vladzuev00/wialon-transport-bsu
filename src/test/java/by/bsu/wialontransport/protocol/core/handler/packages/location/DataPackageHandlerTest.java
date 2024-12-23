//package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;
//
//import by.bsu.wialontransport.config.property.DataDefaultPropertyConfig;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler.NoTrackerInContextException;
//import by.bsu.wialontransport.protocol.core.model.ReceivedData;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import io.netty.channel.ChannelHandlerContext;
//import lombok.AllArgsConstructor;
//import lombok.Value;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static by.bsu.wialontransport.util.OptionalUtil.ofNullableDouble;
//import static by.bsu.wialontransport.util.OptionalUtil.ofNullableInt;
//import static java.util.Collections.emptyMap;
//import static java.util.Optional.ofNullable;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class DataPackageHandlerTest {
//    private static final int GIVEN_DEFAULT_COURSE = 1005;
//    private static final double GIVEN_DEFAULT_SPEED = 1006;
//    private static final int GIVEN_DEFAULT_ALTITUDE = 1000;
//    private static final int GIVEN_DEFAULT_AMOUNT_OF_SATELLITES = 1001;
//    private static final double GIVEN_DEFAULT_HDOP = 1002;
//    private static final int GIVEN_DEFAULT_INPUTS = 1003;
//    private static final int GIVEN_DEFAULT_OUTPUTS = 1004;
//    private static final String GIVEN_DEFAULT_DRIVER_KEY_CODE = "driver key code";
//
//    private static final Package GIVEN_RESPONSE = new Package() {
//    };
//
//    @Mock
//    private DataDefaultPropertyConfig mockedDataDefaultPropertyConfig;
//
//    @Mock
//    private ContextAttributeManager mockedContextAttributeManager;
//
//    @Mock
//    private ReceivedDataValidator mockedReceivedDataValidator;
//
//    @Mock
//    private KafkaInboundDataProducer mockedKafkaInboundDataProducer;
//
//    private TestDataPackageHandler dataPackageHandler;
//
//    @Captor
//    private ArgumentCaptor<Data> dataArgumentCaptor;
//
//    @Before
//    public void initializeDataPackageHandler() {
//        dataPackageHandler = new TestDataPackageHandler(
//                mockedDataDefaultPropertyConfig,
//                mockedContextAttributeManager,
//                mockedReceivedDataValidator,
//                mockedKafkaInboundDataProducer
//        );
//    }
//
//    @Before
//    public void injectGivenDefaultDataProperties() {
//        when(mockedDataDefaultPropertyConfig.getCourse())
//                .thenReturn(GIVEN_DEFAULT_COURSE);
//        when(mockedDataDefaultPropertyConfig.getSpeed())
//                .thenReturn(GIVEN_DEFAULT_SPEED);
//        when(mockedDataDefaultPropertyConfig.getAltitude())
//                .thenReturn(GIVEN_DEFAULT_ALTITUDE);
//        when(mockedDataDefaultPropertyConfig.getAmountOfSatellites())
//                .thenReturn(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES);
//        when(mockedDataDefaultPropertyConfig.getHdop())
//                .thenReturn(GIVEN_DEFAULT_HDOP);
//        when(mockedDataDefaultPropertyConfig.getInputs())
//                .thenReturn(GIVEN_DEFAULT_INPUTS);
//        when(mockedDataDefaultPropertyConfig.getOutputs())
//                .thenReturn(GIVEN_DEFAULT_OUTPUTS);
//        when(mockedDataDefaultPropertyConfig.getDriverKeyCode())
//                .thenReturn(GIVEN_DEFAULT_DRIVER_KEY_CODE);
//    }
//
//    @Test
//    public void packageWithOnlyRequiredPropertiesShouldBeHandledInternallyInCaseExistingLastData() {
//        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
//        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
//        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);
//
//        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
//        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
//        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);
//
//        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
//        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
//        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);
//
//        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
//        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
//        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);
//
//        final List<TestSource> givenSources = List.of(
//                firstGivenSource,
//                secondGivenSource,
//                thirdGivenSource,
//                fourthGivenSource
//        );
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Tracker givenTracker = createTracker(255L);
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));
//
//        final Data givenLastData = createData(
//                256L,
//                LocalDateTime.of(2023, 1, 8, 14, 15, 14)
//        );
//        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.of(givenLastData));
//
//        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                firstGivenSourceDateTime,
//                firstGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                secondGivenSourceDateTime,
//                secondGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                thirdGivenSourceDateTime,
//                thirdGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                fourthGivenSourceDateTime,
//                fourthGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);
//
//        final Package actual = dataPackageHandler.handleInternal(givenRequest, givenContext);
//        assertSame(GIVEN_RESPONSE, actual);
//
//        verify(mockedKafkaInboundDataProducer, times(2)).send(dataArgumentCaptor.capture());
//        final List<Data> actualSentData = dataArgumentCaptor.getAllValues();
//        final List<Data> expectedSentData = List.of(
//                createDataWithOnlyRequiredProperties(secondGivenSourceDateTime, secondGivenSourceCoordinate, givenTracker),
//                createDataWithOnlyRequiredProperties(firstGivenSourceDateTime, firstGivenSourceCoordinate, givenTracker)
//        );
//        assertEquals(expectedSentData, actualSentData);
//    }
//
//    @Test
//    public void packageWithOnlyRequiredPropertiesShouldBeHandledInternallyInCaseNotExistingLastData() {
//        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
//        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
//        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);
//
//        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
//        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
//        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);
//
//        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
//        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
//        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);
//
//        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
//        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
//        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);
//
//        final List<TestSource> givenSources = List.of(
//                firstGivenSource,
//                secondGivenSource,
//                thirdGivenSource,
//                fourthGivenSource
//        );
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Tracker givenTracker = createTracker(256L);
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));
//
//        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.empty());
//
//        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                firstGivenSourceDateTime,
//                firstGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                secondGivenSourceDateTime,
//                secondGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                thirdGivenSourceDateTime,
//                thirdGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                fourthGivenSourceDateTime,
//                fourthGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);
//
//        final Package actual = dataPackageHandler.handleInternal(givenRequest, givenContext);
//        assertSame(GIVEN_RESPONSE, actual);
//
//        verify(mockedKafkaInboundDataProducer, times(3)).send(dataArgumentCaptor.capture());
//        final List<Data> actualSentData = dataArgumentCaptor.getAllValues();
//        final List<Data> expectedSentData = List.of(
//                createDataWithOnlyRequiredProperties(thirdGivenSourceDateTime, thirdGivenSourceCoordinate, givenTracker),
//                createDataWithOnlyRequiredProperties(secondGivenSourceDateTime, secondGivenSourceCoordinate, givenTracker),
//                createDataWithOnlyRequiredProperties(firstGivenSourceDateTime, firstGivenSourceCoordinate, givenTracker)
//        );
//        assertEquals(expectedSentData, actualSentData);
//    }
//
//    @Test
//    public void packageWithoutValidDataAndWithOnlyRequiredPropertiesShouldBeHandledInternally() {
//        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
//        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
//        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);
//
//        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
//        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
//        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);
//
//        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
//        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
//        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);
//
//        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
//        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
//        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);
//
//        final List<TestSource> givenSources = List.of(
//                firstGivenSource,
//                secondGivenSource,
//                thirdGivenSource,
//                fourthGivenSource
//        );
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Tracker givenTracker = createTracker(256L);
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));
//
//        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.empty());
//
//        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                firstGivenSourceDateTime,
//                firstGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(false);
//
//        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                secondGivenSourceDateTime,
//                secondGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(false);
//
//        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                thirdGivenSourceDateTime,
//                thirdGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(false);
//
//        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                fourthGivenSourceDateTime,
//                fourthGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(false);
//
//        final Package actual = dataPackageHandler.handleInternal(givenRequest, givenContext);
//        assertSame(GIVEN_RESPONSE, actual);
//
//        verifyNoInteractions(mockedKafkaInboundDataProducer);
//    }
//
//    @Test
//    public void packageWithOldDataAndWithOnlyRequiredPropertiesShouldBeHandledInternally() {
//        final LocalDateTime firstGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
//        final Coordinate firstGivenSourceCoordinate = new Coordinate(1.1, 1.2);
//        final TestSource firstGivenSource = new TestSource(firstGivenSourceDateTime, firstGivenSourceCoordinate);
//
//        final LocalDateTime secondGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 15);
//        final Coordinate secondGivenSourceCoordinate = new Coordinate(2.1, 2.2);
//        final TestSource secondGivenSource = new TestSource(secondGivenSourceDateTime, secondGivenSourceCoordinate);
//
//        final LocalDateTime thirdGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 13);
//        final Coordinate thirdGivenSourceCoordinate = new Coordinate(3.1, 3.2);
//        final TestSource thirdGivenSource = new TestSource(thirdGivenSourceDateTime, thirdGivenSourceCoordinate);
//
//        final LocalDateTime fourthGivenSourceDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 19);
//        final Coordinate fourthGivenSourceCoordinate = new Coordinate(0, 0);
//        final TestSource fourthGivenSource = new TestSource(fourthGivenSourceDateTime, fourthGivenSourceCoordinate);
//
//        final List<TestSource> givenSources = List.of(
//                firstGivenSource,
//                secondGivenSource,
//                thirdGivenSource,
//                fourthGivenSource
//        );
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Tracker givenTracker = createTracker(255L);
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));
//
//        final Data givenLastData = createData(
//                257L,
//                LocalDateTime.of(2024, 1, 8, 14, 15, 14)
//        );
//        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.of(givenLastData));
//
//        final ReceivedData firstExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                firstGivenSourceDateTime,
//                firstGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(firstExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData secondExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                secondGivenSourceDateTime,
//                secondGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(secondExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData thirdExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                thirdGivenSourceDateTime,
//                thirdGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(thirdExpectedReceivedData))).thenReturn(true);
//
//        final ReceivedData fourthExpectedReceivedData = createReceivedDataWithDefaultProperties(
//                fourthGivenSourceDateTime,
//                fourthGivenSourceCoordinate,
//                givenTracker
//        );
//        when(mockedReceivedDataValidator.isValid(eq(fourthExpectedReceivedData))).thenReturn(true);
//
//        final Package actual = dataPackageHandler.handleInternal(givenRequest, givenContext);
//        assertSame(GIVEN_RESPONSE, actual);
//
//        verifyNoInteractions(mockedKafkaInboundDataProducer);
//    }
//
//    @Test(expected = NoTrackerInContextException.class)
//    public void packageWithOnlyRequiredPropertiesShouldNotBeHandledBecauseOfNoTrackerInContext() {
//        final List<TestSource> givenSources = List.of(
//                new TestSource(
//                        LocalDateTime.of(2023, 1, 8, 14, 15, 16),
//                        new Coordinate(1.1, 1.2)
//                ),
//                new TestSource(
//                        LocalDateTime.of(2023, 1, 8, 14, 15, 15),
//                        new Coordinate(2.1, 2.2)
//                )
//        );
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.empty());
//
//        dataPackageHandler.handleInternal(givenRequest, givenContext);
//    }
//
//    @Test
//    public void packageWithAllPropertiesShouldBeHandledInternallyInCaseNotExistingLastData() {
//        final LocalDateTime givenDateTime = LocalDateTime.of(2023, 1, 8, 14, 15, 16);
//        final Coordinate givenCoordinate = new Coordinate(1.1, 1.2);
//        final int givenCourse = 5;
//        final double givenSpeed = 6;
//        final int givenAltitude = 7;
//        final int givenAmountOfSatellites = 8;
//        final double givenHdop = 9;
//        final int givenInputs = 10;
//        final int givenOutputs = 11;
//        final double[] givenAnalogInputs = {12, 13, 14};
//        final String givenDriverKeyCode = "driver key code";
//
//        final Parameter givenParameter = createParameter(256L, "parameter-name");
//        final Set<Parameter> givenParameters = Set.of(givenParameter);
//
//        final TestSource givenSource = new TestSource(
//                givenDateTime,
//                givenCoordinate,
//                givenCourse,
//                givenSpeed,
//                givenAltitude,
//                givenAmountOfSatellites,
//                givenHdop,
//                givenInputs,
//                givenOutputs,
//                givenAnalogInputs,
//                givenDriverKeyCode,
//                givenParameters
//        );
//        final List<TestSource> givenSources = List.of(givenSource);
//        final TestDataPackage givenRequest = new TestDataPackage(givenSources);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Tracker givenTracker = createTracker(255L);
//        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));
//
//        when(mockedContextAttributeManager.findLastData(same(givenContext))).thenReturn(Optional.empty());
//
//        final ReceivedData expectedReceivedData = ReceivedData.builder()
//                .dateTime(givenDateTime)
//                .coordinate(givenCoordinate)
//                .course(givenCourse)
//                .speed(givenSpeed)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .hdop(givenHdop)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .analogInputs(givenAnalogInputs)
//                .driverKeyCode(givenDriverKeyCode)
//                .parametersByNames(Map.of(givenParameter.getName(), givenParameter))
//                .tracker(givenTracker)
//                .build();
//        when(mockedReceivedDataValidator.isValid(eq(expectedReceivedData))).thenReturn(true);
//
//        final Package actual = dataPackageHandler.handleInternal(givenRequest, givenContext);
//        assertSame(GIVEN_RESPONSE, actual);
//
//        verify(mockedKafkaInboundDataProducer, times(1)).send(dataArgumentCaptor.capture());
//        final List<Data> actualSentData = dataArgumentCaptor.getAllValues();
//        final List<Data> expectedSentData = List.of(
//                Data.builder()
//                        .dateTime(givenDateTime)
//                        .coordinate(givenCoordinate)
//                        .course(givenCourse)
//                        .speed(givenSpeed)
//                        .altitude(givenAltitude)
//                        .amountOfSatellites(givenAmountOfSatellites)
//                        .hdop(givenHdop)
//                        .inputs(givenInputs)
//                        .outputs(givenOutputs)
//                        .analogInputs(givenAnalogInputs)
//                        .driverKeyCode(givenDriverKeyCode)
//                        .parametersByNames(Map.of(givenParameter.getName(), givenParameter))
//                        .tracker(givenTracker)
//                        .build()
//        );
//        assertEquals(expectedSentData, actualSentData);
//    }
//
//    private static Tracker createTracker(final Long id) {
//        return Tracker.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Data createData(final Long id, final LocalDateTime dateTime) {
//        return Data.builder()
//                .id(id)
//                .dateTime(dateTime)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static Parameter createParameter(final Long id, final String name) {
//        return Parameter.builder()
//                .id(id)
//                .name(name)
//                .build();
//    }
//
//    private static ReceivedData createReceivedDataWithDefaultProperties(final LocalDateTime dateTime,
//                                                                        final Coordinate coordinate,
//                                                                        final Tracker tracker) {
//        return ReceivedData.builder()
//                .dateTime(dateTime)
//                .coordinate(coordinate)
//                .course(GIVEN_DEFAULT_COURSE)
//                .speed(GIVEN_DEFAULT_SPEED)
//                .altitude(GIVEN_DEFAULT_ALTITUDE)
//                .amountOfSatellites(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES)
//                .hdop(GIVEN_DEFAULT_HDOP)
//                .inputs(GIVEN_DEFAULT_INPUTS)
//                .outputs(GIVEN_DEFAULT_OUTPUTS)
//                .analogInputs(new double[]{})
//                .driverKeyCode(GIVEN_DEFAULT_DRIVER_KEY_CODE)
//                .parametersByNames(emptyMap())
//                .tracker(tracker)
//                .build();
//    }
//
//    private static Data createDataWithOnlyRequiredProperties(final LocalDateTime dateTime,
//                                                             final Coordinate coordinate,
//                                                             final Tracker tracker) {
//        return Data.builder()
//                .dateTime(dateTime)
//                .coordinate(coordinate)
//                .course(GIVEN_DEFAULT_COURSE)
//                .speed(GIVEN_DEFAULT_SPEED)
//                .altitude(GIVEN_DEFAULT_ALTITUDE)
//                .amountOfSatellites(GIVEN_DEFAULT_AMOUNT_OF_SATELLITES)
//                .hdop(GIVEN_DEFAULT_HDOP)
//                .inputs(GIVEN_DEFAULT_INPUTS)
//                .outputs(GIVEN_DEFAULT_OUTPUTS)
//                .analogInputs(new double[]{})
//                .driverKeyCode(GIVEN_DEFAULT_DRIVER_KEY_CODE)
//                .parametersByNames(emptyMap())
//                .tracker(tracker)
//                .build();
//    }
//
//    @Value
//    @AllArgsConstructor
//    private static class TestSource {
//        LocalDateTime dateTime;
//        Coordinate coordinate;
//        Integer course;
//        Double speed;
//        Integer altitude;
//        Integer amountOfSatellites;
//        Double hdop;
//        Integer inputs;
//        Integer outputs;
//        double[] analogInputs;
//        String driverKeyCode;
//        Set<Parameter> parameters;
//
//        public TestSource(final LocalDateTime dateTime, final Coordinate coordinate) {
//            this.dateTime = dateTime;
//            this.coordinate = coordinate;
//            course = null;
//            speed = null;
//            altitude = null;
//            amountOfSatellites = null;
//            hdop = null;
//            inputs = null;
//            outputs = null;
//            analogInputs = null;
//            driverKeyCode = null;
//            parameters = new HashSet<>();
//        }
//    }
//
//    @Value
//    private static class TestDataPackage implements Package {
//        List<TestSource> sources;
//    }
//
//    private static final class TestDataPackageHandler extends DataPackageHandler<TestDataPackage, TestSource> {
//
//        public TestDataPackageHandler(final DataDefaultPropertyConfig dataDefaultPropertyConfig,
//                                      final ContextAttributeManager contextAttributeManager,
//                                      final ReceivedDataValidator receivedDataValidator,
//                                      final KafkaInboundDataProducer kafkaInboundDataProducer) {
//            super(
//                    TestDataPackage.class,
//                    dataDefaultPropertyConfig,
//                    contextAttributeManager,
//                    receivedDataValidator,
//                    kafkaInboundDataProducer
//            );
//        }
//
//        @Override
//        protected List<TestSource> getSources(final TestDataPackage request) {
//            return request.sources;
//        }
//
//        @Override
//        protected LocalDateTime getDateTime(final TestSource source) {
//            return source.getDateTime();
//        }
//
//        @Override
//        protected Coordinate getCoordinate(final TestSource source) {
//            return source.getCoordinate();
//        }
//
//        @Override
//        protected OptionalInt findCourse(final TestSource source) {
//            return ofNullableInt(source.course);
//        }
//
//        @Override
//        protected OptionalDouble findSpeed(final TestSource source) {
//            return ofNullableDouble(source.speed);
//        }
//
//        @Override
//        protected OptionalInt findAltitude(final TestSource source) {
//            return ofNullableInt(source.altitude);
//        }
//
//        @Override
//        protected OptionalInt findAmountOfSatellites(final TestSource source) {
//            return ofNullableInt(source.amountOfSatellites);
//        }
//
//        @Override
//        protected OptionalDouble findHdop(final TestSource source) {
//            return ofNullableDouble(source.hdop);
//        }
//
//        @Override
//        protected OptionalInt findInputs(final TestSource source) {
//            return ofNullableInt(source.inputs);
//        }
//
//        @Override
//        protected OptionalInt findOutputs(final TestSource source) {
//            return ofNullableInt(source.outputs);
//        }
//
//        @Override
//        protected Optional<double[]> findAnalogInputs(final TestSource source) {
//            return ofNullable(source.analogInputs);
//        }
//
//        @Override
//        protected Optional<String> findDriverKeyCode(final TestSource source) {
//            return ofNullable(source.driverKeyCode);
//        }
//
//        @Override
//        protected Optional<Set<Parameter>> findParameters(final TestSource source) {
//            return ofNullable(source.getParameters());
//        }
//
//        @Override
//        protected Package createResponse(final TestDataPackage request) {
//            return GIVEN_RESPONSE;
//        }
//    }
//}
