package by.bsu.wialontransport.protocol.core.handler.packages.location;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.OptionalUtil.ofNullableDouble;
import static by.bsu.wialontransport.util.OptionalUtil.ofNullableInt;
import static java.util.Optional.ofNullable;

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
    public void requestWithDefinedPropertiesShouldBeHandledInternallyInCaseExistingLastData() {
        final TestLocationSource firstGivenLocation = new TestLocationSource(
                LocalDate.of(2024, 11, 15),
                LocalTime.of(12, 36, 37),
                55.3432,
                27.5643,
                10,
                60.,
                50,

        );
    }

    @Value
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
            return request.getParameters().stream();
        }

        @Override
        protected TestResponse createResponse(final int locationCount) {
            return new TestResponse(locationCount);
        }
    }
}
