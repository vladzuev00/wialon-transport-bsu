package by.vladzuev.locationreceiver.protocol.core.handler.packages.location;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static by.vladzuev.locationreceiver.crud.enumeration.ParameterType.INTEGER;
import static by.vladzuev.locationreceiver.util.OptionalUtil.ofNullableDouble;
import static by.vladzuev.locationreceiver.util.OptionalUtil.ofNullableInt;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class LocationPackageHandlerTest {

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private LocationDefaultProperty mockedLocationDefaultProperty;

    @Mock
    private LocationValidator mockedLocationValidator;

    @Mock
    private KafkaInboundLocationProducer mockedLocationProducer;

    private TestLocationPackageHandler handler;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestLocationPackageHandler(
                mockedContextAttributeManager,
                mockedLocationDefaultProperty,
                mockedLocationValidator,
                mockedLocationProducer
        );
    }

    @Test
    public void requestShouldBeHandledInternallyInCaseExistingLastLocation() {
        final TestLocationPackage givenRequest = new TestLocationPackage(
                List.of(
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 16),
                                LocalTime.of(12, 36, 38),
                                55.3433,
                                27.5644,
                                11,
                                61.,
                                51,
                                16,
                                7.8,
                                21,
                                22,
                                new double[]{4, 5, 6, 7},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 15),
                                LocalTime.of(12, 36, 37),
                                55.3432,
                                27.5643,
                                10,
                                60.,
                                50,
                                15,
                                7.7,
                                20,
                                21,
                                new double[]{4, 5, 6},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        TestLocationSource.builder().parameters(Collections.emptySet()).build(),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 17),
                                LocalTime.of(12, 36, 39),
                                55.3434,
                                27.5645,
                                12,
                                62.,
                                52,
                                17,
                                7.9,
                                22,
                                23,
                                new double[]{4, 5, 6, 7, 8},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        )
                )
        );
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = Tracker.builder().id(255L).build();
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        final Location givenLastLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 15, 12, 36, 38))
                .build();
        when(mockedContextAttributeManager.findLastLocation(same(givenContext)))
                .thenReturn(Optional.of(givenLastLocation));

        final LocalDate givenDefaultDate = LocalDate.of(2024, 11, 15);
        when(mockedLocationDefaultProperty.getDate()).thenReturn(givenDefaultDate);

        final LocalTime givenDefaultTime = LocalTime.of(12, 36, 39);
        when(mockedLocationDefaultProperty.getTime()).thenReturn(givenDefaultTime);

        final double givenDefaultLatitude = 50.;
        when(mockedLocationDefaultProperty.getLatitude()).thenReturn(givenDefaultLatitude);

        final double givenDefaultLongitude = 60.;
        when(mockedLocationDefaultProperty.getLongitude()).thenReturn(givenDefaultLongitude);

        final int givenDefaultCourse = 70;
        when(mockedLocationDefaultProperty.getCourse()).thenReturn(givenDefaultCourse);

        final double givenDefaultSpeed = 80;
        when(mockedLocationDefaultProperty.getSpeed()).thenReturn(givenDefaultSpeed);

        final int givenDefaultAltitude = 90;
        when(mockedLocationDefaultProperty.getAltitude()).thenReturn(givenDefaultAltitude);

        final int givenDefaultSatelliteCount = 15;
        when(mockedLocationDefaultProperty.getSatelliteCount()).thenReturn(givenDefaultSatelliteCount);

        final double givenDefaultHdop = 5.5;
        when(mockedLocationDefaultProperty.getHdop()).thenReturn(givenDefaultHdop);

        final int givenDefaultInputs = 20;
        when(mockedLocationDefaultProperty.getInputs()).thenReturn(givenDefaultInputs);

        final int givenDefaultOutputs = 30;
        when(mockedLocationDefaultProperty.getOutputs()).thenReturn(givenDefaultOutputs);

        final String givenDefaultDriverKeyCode = "default-driver-key-code";
        when(mockedLocationDefaultProperty.getDriverKeyCode()).thenReturn(givenDefaultDriverKeyCode);

        final Location expectedFirstLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 15, 12, 36, 37))
                .coordinate(new GpsCoordinate(55.3432, 27.5643))
                .course(10)
                .speed(60.)
                .altitude(50)
                .satelliteCount(15)
                .hdop(7.7)
                .inputs(20)
                .outputs(21)
                .analogInputs(new double[]{4, 5, 6})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFirstLocation))).thenReturn(true);

        final Location expectedSecondLocation = Location.builder()
                .dateTime(LocalDateTime.of(givenDefaultDate, givenDefaultTime))
                .coordinate(new GpsCoordinate(givenDefaultLatitude, givenDefaultLongitude))
                .course(givenDefaultCourse)
                .speed(givenDefaultSpeed)
                .altitude(givenDefaultAltitude)
                .satelliteCount(givenDefaultSatelliteCount)
                .hdop(givenDefaultHdop)
                .inputs(givenDefaultInputs)
                .outputs(givenDefaultOutputs)
                .driverKeyCode(givenDefaultDriverKeyCode)
                .tracker(givenTracker)
                .parametersByNames(emptyMap())
                .build();
        when(mockedLocationValidator.isValid(eq(expectedSecondLocation))).thenReturn(true);

        final Location expectedThirdLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 16, 12, 36, 38))
                .coordinate(new GpsCoordinate(55.3433, 27.5644))
                .course(11)
                .speed(61)
                .altitude(51)
                .satelliteCount(16)
                .hdop(7.8)
                .inputs(21)
                .outputs(22)
                .analogInputs(new double[]{4, 5, 6, 7})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedThirdLocation))).thenReturn(true);

        final Location expectedFourthLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 17, 12, 36, 39))
                .coordinate(new GpsCoordinate(55.3434, 27.5645))
                .course(12)
                .speed(62)
                .altitude(52)
                .satelliteCount(17)
                .hdop(7.9)
                .inputs(22)
                .outputs(23)
                .analogInputs(new double[]{4, 5, 6, 7, 8})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFourthLocation))).thenReturn(false);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponse expected = new TestResponse(4);
        assertEquals(expected, actual);

        verify(mockedLocationProducer, times(2)).produce(locationCaptor.capture());
        verify(mockedContextAttributeManager, times(1)).putLastLocation(same(givenContext), locationCaptor.capture());

        final List<Location> actualCapturedLocations = locationCaptor.getAllValues();
        final List<Location> expectedCapturedLocations = List.of(
                expectedSecondLocation,
                expectedThirdLocation,
                expectedThirdLocation
        );
        assertEquals(expectedCapturedLocations, actualCapturedLocations);
    }

    @Test
    public void requestShouldBeHandledInternallyInCaseNotExistingLastLocation() {
        final TestLocationPackage givenRequest = new TestLocationPackage(
                List.of(
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 15),
                                LocalTime.of(12, 36, 37),
                                55.3432,
                                27.5643,
                                10,
                                60.,
                                50,
                                15,
                                7.7,
                                20,
                                21,
                                new double[]{4, 5, 6},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        TestLocationSource.builder().parameters(Collections.emptySet()).build(),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 16),
                                LocalTime.of(12, 36, 38),
                                55.3433,
                                27.5644,
                                11,
                                61.,
                                51,
                                16,
                                7.8,
                                21,
                                22,
                                new double[]{4, 5, 6, 7},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 17),
                                LocalTime.of(12, 36, 39),
                                55.3434,
                                27.5645,
                                12,
                                62.,
                                52,
                                17,
                                7.9,
                                22,
                                23,
                                new double[]{4, 5, 6, 7, 8},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        )
                )
        );
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = Tracker.builder().id(255L).build();
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        when(mockedContextAttributeManager.findLastLocation(same(givenContext))).thenReturn(empty());

        final LocalDate givenDefaultDate = LocalDate.of(2024, 11, 15);
        when(mockedLocationDefaultProperty.getDate()).thenReturn(givenDefaultDate);

        final LocalTime givenDefaultTime = LocalTime.of(12, 36, 39);
        when(mockedLocationDefaultProperty.getTime()).thenReturn(givenDefaultTime);

        final double givenDefaultLatitude = 50.;
        when(mockedLocationDefaultProperty.getLatitude()).thenReturn(givenDefaultLatitude);

        final double givenDefaultLongitude = 60.;
        when(mockedLocationDefaultProperty.getLongitude()).thenReturn(givenDefaultLongitude);

        final int givenDefaultCourse = 70;
        when(mockedLocationDefaultProperty.getCourse()).thenReturn(givenDefaultCourse);

        final double givenDefaultSpeed = 80;
        when(mockedLocationDefaultProperty.getSpeed()).thenReturn(givenDefaultSpeed);

        final int givenDefaultAltitude = 90;
        when(mockedLocationDefaultProperty.getAltitude()).thenReturn(givenDefaultAltitude);

        final int givenDefaultSatelliteCount = 15;
        when(mockedLocationDefaultProperty.getSatelliteCount()).thenReturn(givenDefaultSatelliteCount);

        final double givenDefaultHdop = 5.5;
        when(mockedLocationDefaultProperty.getHdop()).thenReturn(givenDefaultHdop);

        final int givenDefaultInputs = 20;
        when(mockedLocationDefaultProperty.getInputs()).thenReturn(givenDefaultInputs);

        final int givenDefaultOutputs = 30;
        when(mockedLocationDefaultProperty.getOutputs()).thenReturn(givenDefaultOutputs);

        final String givenDefaultDriverKeyCode = "default-driver-key-code";
        when(mockedLocationDefaultProperty.getDriverKeyCode()).thenReturn(givenDefaultDriverKeyCode);

        final Location expectedFirstLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 15, 12, 36, 37))
                .coordinate(new GpsCoordinate(55.3432, 27.5643))
                .course(10)
                .speed(60.)
                .altitude(50)
                .satelliteCount(15)
                .hdop(7.7)
                .inputs(20)
                .outputs(21)
                .analogInputs(new double[]{4, 5, 6})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFirstLocation))).thenReturn(true);

        final Location expectedSecondLocation = Location.builder()
                .dateTime(LocalDateTime.of(givenDefaultDate, givenDefaultTime))
                .coordinate(new GpsCoordinate(givenDefaultLatitude, givenDefaultLongitude))
                .course(givenDefaultCourse)
                .speed(givenDefaultSpeed)
                .altitude(givenDefaultAltitude)
                .satelliteCount(givenDefaultSatelliteCount)
                .hdop(givenDefaultHdop)
                .inputs(givenDefaultInputs)
                .outputs(givenDefaultOutputs)
                .driverKeyCode(givenDefaultDriverKeyCode)
                .tracker(givenTracker)
                .parametersByNames(emptyMap())
                .build();
        when(mockedLocationValidator.isValid(eq(expectedSecondLocation))).thenReturn(true);

        final Location expectedThirdLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 16, 12, 36, 38))
                .coordinate(new GpsCoordinate(55.3433, 27.5644))
                .course(11)
                .speed(61)
                .altitude(51)
                .satelliteCount(16)
                .hdop(7.8)
                .inputs(21)
                .outputs(22)
                .analogInputs(new double[]{4, 5, 6, 7})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedThirdLocation))).thenReturn(true);

        final Location expectedFourthLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 17, 12, 36, 39))
                .coordinate(new GpsCoordinate(55.3434, 27.5645))
                .course(12)
                .speed(62)
                .altitude(52)
                .satelliteCount(17)
                .hdop(7.9)
                .inputs(22)
                .outputs(23)
                .analogInputs(new double[]{4, 5, 6, 7, 8})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build()))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFourthLocation))).thenReturn(false);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponse expected = new TestResponse(4);
        assertEquals(expected, actual);

        verify(mockedLocationProducer, times(3)).produce(locationCaptor.capture());
        verify(mockedContextAttributeManager, times(1)).putLastLocation(same(givenContext), locationCaptor.capture());

        final List<Location> actualCapturedLocations = locationCaptor.getAllValues();
        final List<Location> expectedCapturedLocations = List.of(
                expectedFirstLocation,
                expectedSecondLocation,
                expectedThirdLocation,
                expectedThirdLocation
        );
        Assertions.assertEquals(expectedCapturedLocations, actualCapturedLocations);
    }

    @Test
    public void requestShouldNotBeHandledInternallyBecauseOfNoTrackerInContext() {
        final TestLocationPackage givenRequest = new TestLocationPackage(
                List.of(
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 15),
                                LocalTime.of(12, 36, 37),
                                55.3432,
                                27.5643,
                                10,
                                60.,
                                50,
                                15,
                                7.7,
                                20,
                                21,
                                new double[]{4, 5, 6},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        TestLocationSource.builder().build(),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 16),
                                LocalTime.of(12, 36, 38),
                                55.3433,
                                27.5644,
                                11,
                                61.,
                                51,
                                16,
                                7.8,
                                21,
                                22,
                                new double[]{4, 5, 6, 7},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        ),
                        new TestLocationSource(
                                LocalDate.of(2024, 11, 17),
                                LocalTime.of(12, 36, 39),
                                55.3434,
                                27.5645,
                                12,
                                62.,
                                52,
                                17,
                                7.9,
                                22,
                                23,
                                new double[]{4, 5, 6, 7, 8},
                                "test-driver-key-code",
                                Set.of(Parameter.builder().id(255L).name("test-name").type(INTEGER).value("10").build())
                        )
                )
        );
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(empty());

        assertThrows(IllegalArgumentException.class, () -> handler.handleInternal(givenRequest, givenContext));

        verifyNoInteractions(mockedLocationDefaultProperty, mockedLocationValidator, mockedLocationProducer);
    }

    @Value
    @Builder
    @AllArgsConstructor
    private static class TestLocationSource {
        LocalDate date;
        LocalTime time;
        Double latitude;
        Double longitude;
        Integer course;
        Double speed;
        Integer altitude;
        Integer satelliteCount;
        Double hdop;
        Integer inputs;
        Integer outputs;
        double[] analogInputs;
        String driverKeyCode;
        Set<Parameter> parameters;
    }

    @Value
    private static class TestLocationPackage {
        List<TestLocationSource> locations;
    }

    @Value
    private static class TestResponse {
        int locationCount;
    }

    private static final class TestLocationPackageHandler extends LocationPackageHandler<TestLocationSource, TestLocationPackage> {

        public TestLocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                          final LocationDefaultProperty locationDefaultProperty,
                                          final LocationValidator locationValidator,
                                          final KafkaInboundLocationProducer locationProducer) {
            super(
                    TestLocationPackage.class,
                    contextAttributeManager,
                    locationDefaultProperty,
                    locationValidator,
                    locationProducer
            );
        }

        @Override
        protected Stream<TestLocationSource> streamLocationSources(final TestLocationPackage request) {
            return request.getLocations().stream();
        }

        @Override
        protected Optional<LocalDate> findDate(final TestLocationSource location) {
            return ofNullable(location.getDate());
        }

        @Override
        protected Optional<LocalTime> findTime(final TestLocationSource location) {
            return ofNullable(location.getTime());
        }

        @Override
        protected OptionalDouble findLatitude(final TestLocationSource location) {
            return ofNullableDouble(location.getLatitude());
        }

        @Override
        protected OptionalDouble findLongitude(final TestLocationSource location) {
            return ofNullableDouble(location.getLongitude());
        }

        @Override
        protected OptionalInt findCourse(final TestLocationSource location) {
            return ofNullableInt(location.getCourse());
        }

        @Override
        protected OptionalDouble findSpeed(final TestLocationSource location) {
            return ofNullableDouble(location.getSpeed());
        }

        @Override
        protected OptionalInt findAltitude(final TestLocationSource location) {
            return ofNullableInt(location.getAltitude());
        }

        @Override
        protected OptionalInt findSatelliteCount(final TestLocationSource location) {
            return ofNullableInt(location.getSatelliteCount());
        }

        @Override
        protected OptionalDouble findHdop(final TestLocationSource location) {
            return ofNullableDouble(location.getHdop());
        }

        @Override
        protected OptionalInt findInputs(final TestLocationSource location) {
            return ofNullableInt(location.getInputs());
        }

        @Override
        protected OptionalInt findOutputs(final TestLocationSource location) {
            return ofNullableInt(location.getOutputs());
        }

        @Override
        protected double[] getAnalogInputs(final TestLocationSource location) {
            return location.getAnalogInputs();
        }

        @Override
        protected Optional<String> findDriverKeyCode(final TestLocationSource location) {
            return ofNullable(location.getDriverKeyCode());
        }

        @Override
        protected Stream<Parameter> streamParameters(final TestLocationSource location) {
            return location.getParameters().stream();
        }

        @Override
        protected TestResponse createResponse(final TestLocationPackage request) {
            return new TestResponse(request.getLocations().size());
        }
    }
}
