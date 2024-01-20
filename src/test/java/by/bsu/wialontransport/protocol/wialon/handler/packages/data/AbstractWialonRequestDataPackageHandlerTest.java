package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;
import lombok.Value;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.WESTERN;
import static org.junit.Assert.*;

public final class AbstractWialonRequestDataPackageHandlerTest {

    private final TestWialonRequestDataPackageHandler handler = new TestWialonRequestDataPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void sourcesShouldBeGot() {
        final List<WialonData> givenSources = List.of(
                WialonData.builder()
                        .date(LocalDate.of(2024, 1, 20))
                        .build(),
                WialonData.builder()
                        .date(LocalDate.of(2024, 1, 21))
                        .build()
        );
        final TestWialonRequestDataPackage givenRequest = new TestWialonRequestDataPackage(givenSources);

        final List<WialonData> actual = handler.getSources(givenRequest);
        assertSame(givenSources, actual);
    }

    @Test
    public void dateTimeShouldBeGot() {
        final WialonData givenSource = WialonData.builder()
                .date(LocalDate.of(2024, 1, 21))
                .time(LocalTime.of(22, 1, 3))
                .build();

        final LocalDateTime actual = handler.getDateTime(givenSource);
        final LocalDateTime expected = LocalDateTime.of(2024, 1, 21, 22, 1, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void coordinateShouldBeGot() {
        final WialonData givenSource = WialonData.builder()
                .latitude(
                        Latitude.builder()
                                .degrees(40)
                                .minutes(45)
                                .minuteShare(36)
                                .hemisphere(NORTH)
                                .build()
                )
                .longitude(
                        Longitude.builder()
                                .degrees(73)
                                .minutes(59)
                                .minuteShare(36)
                                .hemisphere(WESTERN)
                                .build()
                )
                .build();

        final Coordinate actual = handler.getCoordinate(givenSource);
        final Coordinate expected = new Coordinate(40.76, -73.99333333333334);
        assertEquals(expected, actual);
    }

    @Test
    public void courseShouldBeFound() {
        final int givenCourse = 4;
        final WialonData givenSource = WialonData.builder()
                .course(givenCourse)
                .build();

        final OptionalInt optionalActual = handler.findCourse(givenSource);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenCourse, actual);
    }

    @Test
    public void courseShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalInt optionalActual = handler.findCourse(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void speedShouldBeFound() {
        final double givenSpeed = 4.4;
        final WialonData givenSource = WialonData.builder()
                .speed(givenSpeed)
                .build();

        final OptionalDouble optionalActual = handler.findSpeed(givenSource);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenSpeed, actual, 0.);
    }

    @Test
    public void speedShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalDouble optionalActual = handler.findSpeed(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void altitudeShouldBeFound() {
        final int givenAltitude = 10;
        final WialonData givenSource = WialonData.builder()
                .altitude(givenAltitude)
                .build();

        final OptionalInt optionalActual = handler.findAltitude(givenSource);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void altitudeShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalInt optionalActual = handler.findAltitude(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void amountOfSatellitesShouldBeFound() {
        final int givenAmountOfSatellites = 10;
        final WialonData givenSource = WialonData.builder()
                .amountOfSatellites(givenAmountOfSatellites)
                .build();

        final OptionalInt optionalActual = handler.findAmountOfSatellites(givenSource);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenAmountOfSatellites, actual);
    }

    @Test
    public void amountOfSatellitesShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalInt optionalActual = handler.findAmountOfSatellites(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void hdopShouldBeFound() {
        final double givenHdop = 4.4;
        final WialonData givenSource = WialonData.builder()
                .hdop(givenHdop)
                .build();

        final OptionalDouble optionalActual = handler.findHdop(givenSource);
        assertTrue(optionalActual.isPresent());
        final double actual = optionalActual.getAsDouble();
        assertEquals(givenHdop, actual, 0.);
    }

    @Test
    public void hdopShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalDouble optionalActual = handler.findHdop(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void inputsShouldBeFound() {
        final int givenInputs = 11;
        final WialonData givenSource = WialonData.builder()
                .inputs(givenInputs)
                .build();

        final OptionalInt optionalActual = handler.findInputs(givenSource);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenInputs, actual);
    }

    @Test
    public void inputsShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalInt optionalActual = handler.findInputs(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void outputsShouldBeFound() {
        final int givenOutputs = 15;
        final WialonData givenSource = WialonData.builder()
                .outputs(givenOutputs)
                .build();

        final OptionalInt optionalActual = handler.findOutputs(givenSource);
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenOutputs, actual);
    }

    @Test
    public void outputsShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final OptionalInt optionalActual = handler.findOutputs(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void analogInputsShouldBeFound() {
        final double[] givenAnalogInputs = {1.1, 2.2, 3.3};
        final WialonData givenSource = WialonData.builder()
                .analogInputs(givenAnalogInputs)
                .build();

        final Optional<double[]> optionalActual = handler.findAnalogInputs(givenSource);
        assertTrue(optionalActual.isPresent());
        final double[] actual = optionalActual.get();
        assertSame(givenAnalogInputs, actual);
    }

    @Test
    public void analogInputsShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final Optional<double[]> optionalActual = handler.findAnalogInputs(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void driverKeyCodeShouldBeFound() {
        final String givenDriverKeyCode = "code";
        final WialonData givenSource = WialonData.builder()
                .driverKeyCode(givenDriverKeyCode)
                .build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenSource);
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        assertSame(givenDriverKeyCode, actual);
    }

    @Test
    public void driverKeyCodeShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final Optional<String> optionalActual = handler.findDriverKeyCode(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersShouldBeFound() {
        final Set<Parameter> givenParameters = Set.of(
                createParameter(255L),
                createParameter(256L)
        );
        final WialonData givenSource = WialonData.builder()
                .parameters(givenParameters)
                .build();

        final Optional<Set<Parameter>> optionalActual = handler.findParameters(givenSource);
        assertTrue(optionalActual.isPresent());
        final Set<Parameter> actual = optionalActual.get();
        assertSame(givenParameters, actual);
    }

    @Test
    public void parametersShouldNotBeFound() {
        final WialonData givenSource = WialonData.builder().build();

        final Optional<Set<Parameter>> optionalActual = handler.findParameters(givenSource);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void responseShouldBeCreated() {
        final TestWialonRequestDataPackage givenRequest = new TestWialonRequestDataPackage(
                List.of(
                        WialonData.builder().build(),
                        WialonData.builder().build(),
                        WialonData.builder().build()
                )
        );

        final Package actual = handler.createResponse(givenRequest);
        final Package expected = new TestWialonResponseDataPackage(3);
        assertEquals(expected, actual);
    }

    private static Parameter createParameter(final Long id) {
        return Parameter.builder()
                .id(id)
                .build();
    }

    private static final class TestWialonRequestDataPackage extends AbstractWialonRequestDataPackage {

        public TestWialonRequestDataPackage(final List<WialonData> data) {
            super(data);
        }
    }

    @Value
    private static class TestWialonResponseDataPackage implements Package {
        int receivedDataCount;
    }

    private static final class TestWialonRequestDataPackageHandler extends AbstractWialonRequestDataPackageHandler<
            TestWialonRequestDataPackage
            > {

        public TestWialonRequestDataPackageHandler(final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                                   final ContextAttributeManager contextAttributeManager,
                                                   final ReceivedDataValidator receivedDataValidator,
                                                   final KafkaInboundDataProducer kafkaInboundDataProducer) {
            super(
                    TestWialonRequestDataPackage.class,
                    dataDefaultPropertyConfiguration,
                    contextAttributeManager,
                    receivedDataValidator,
                    kafkaInboundDataProducer
            );
        }

        @Override
        protected Package createResponse(final int receivedDataCount) {
            return new TestWialonResponseDataPackage(receivedDataCount);
        }
    }
}
