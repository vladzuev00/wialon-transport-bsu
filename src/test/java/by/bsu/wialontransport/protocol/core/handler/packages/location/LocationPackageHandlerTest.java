package by.bsu.wialontransport.protocol.core.handler.packages.location;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.model.ParameterType;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.OptionalUtil.ofNullableDouble;
import static by.bsu.wialontransport.util.OptionalUtil.ofNullableInt;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void requestBeHandledInternallyInCaseExistingLastLocation() {
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
                                Set.of(new Parameter(255L, "test-name", ParameterType.INTEGER, "10"))
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
                                Set.of(new Parameter(255L, "test-name", ParameterType.INTEGER, "10"))
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
                                Set.of(new Parameter(255L, "test-name", ParameterType.INTEGER, "10"))
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

        final double givenDefaultSpeed = 80.;
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

        Location expectedFirstLocation = Location.builder()
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
                .parametersByNames(Map.of("test-name", new Parameter(255L, "test-name", ParameterType.INTEGER, "10")))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFirstLocation))).thenReturn(true);

        Location expectedSecondLocation = Location.builder()
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
                .parametersByNames(Map.of("test-name", new Parameter(255L, "test-name", ParameterType.INTEGER, "10")))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedSecondLocation))).thenReturn(true);

        Location expectedThirdLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 16, 12, 36, 38))
                .coordinate(new GpsCoordinate(55.3433, 27.5644))
                .course(11)
                .speed(61.)
                .altitude(51)
                .satelliteCount(16)
                .hdop(7.8)
                .inputs(21)
                .outputs(22)
                .analogInputs(new double[]{4, 5, 6, 7})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", new Parameter(255L, "test-name", ParameterType.INTEGER, "10")))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedThirdLocation))).thenReturn(true);

        Location expectedFourthLocation = Location.builder()
                .dateTime(LocalDateTime.of(2024, 11, 17, 12, 36, 39))
                .coordinate(new GpsCoordinate(55.3434, 27.5645))
                .course(12)
                .speed(62.)
                .altitude(52)
                .satelliteCount(17)
                .hdop(7.9)
                .inputs(22)
                .outputs(23)
                .analogInputs(new double[]{4, 5, 6, 7, 8})
                .driverKeyCode("test-driver-key-code")
                .parametersByNames(Map.of("test-name", new Parameter(255L, "test-name", ParameterType.INTEGER, "10")))
                .tracker(givenTracker)
                .build();
        when(mockedLocationValidator.isValid(eq(expectedFourthLocation))).thenReturn(false);

        Object actual = handler.handleInternal(givenRequest, givenContext);
        TestResponse expected = new TestResponse(4);
        assertEquals(expected, actual);

        //TODO: verify location producing
        //TODO: verify put last location in context
        throw new UnsupportedOperationException();
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
        protected List<TestLocationSource> getLocationSources(final TestLocationPackage request) {
            return request.getLocations();
        }

        @Override
        protected Optional<LocalDate> findDate(final TestLocationSource request) {
            return ofNullable(request.getDate());
        }

        @Override
        protected Optional<LocalTime> findTime(final TestLocationSource request) {
            return ofNullable(request.getTime());
        }

        @Override
        protected OptionalDouble findLatitude(final TestLocationSource request) {
            return ofNullableDouble(request.getLatitude());
        }

        @Override
        protected OptionalDouble findLongitude(final TestLocationSource request) {
            return ofNullableDouble(request.getLongitude());
        }

        @Override
        protected OptionalInt findCourse(final TestLocationSource request) {
            return ofNullableInt(request.getCourse());
        }

        @Override
        protected OptionalDouble findSpeed(final TestLocationSource request) {
            return ofNullableDouble(request.getSpeed());
        }

        @Override
        protected OptionalInt findAltitude(final TestLocationSource request) {
            return ofNullableInt(request.getAltitude());
        }

        @Override
        protected OptionalInt findSatelliteCount(final TestLocationSource request) {
            return ofNullableInt(request.getSatelliteCount());
        }

        @Override
        protected OptionalDouble findHdop(final TestLocationSource request) {
            return ofNullableDouble(request.getHdop());
        }

        @Override
        protected OptionalInt findInputs(final TestLocationSource request) {
            return ofNullableInt(request.getInputs());
        }

        @Override
        protected OptionalInt findOutputs(final TestLocationSource request) {
            return ofNullableInt(request.getOutputs());
        }

        @Override
        protected double[] getAnalogInputs(final TestLocationSource request) {
            return request.getAnalogInputs();
        }

        @Override
        protected Optional<String> findDriverKeyCode(final TestLocationSource request) {
            return ofNullable(request.getDriverKeyCode());
        }

        @Override
        protected Stream<Parameter> getParameters(final TestLocationSource request) {
            return ofNullable(request.getParameters())
                    .stream()
                    .flatMap(Collection::stream);
        }

        @Override
        protected TestResponse createResponse(final int locationCount) {
            return new TestResponse(locationCount);
        }
    }
}
