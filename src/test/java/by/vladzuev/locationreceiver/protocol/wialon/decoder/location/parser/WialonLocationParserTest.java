package by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

public final class WialonLocationParserTest {
    private final WialonLocationParser parser = new WialonLocationParser();

    @Test
    public void locationWithDefinedComponentsShouldBeParsed() {
        final String givenSource = "test-source";
        final LocalDate givenDate = LocalDate.of(2024, 10, 30);
        final LocalTime givenTime = LocalTime.of(17, 6, 7);
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;
        final double givenSpeed = 7;
        final int givenCourse = 8;
        final int givenAltitude = 9;
        final int givenSatelliteCount = 10;
        final double givenHdop = 11;
        final int givenInputs = 12;
        final int givenOutputs = 13;
        final double[] givenAnalogInputs = {1, 2, 3, 4};
        final String givenDriverKeyCode = "driver-key-code";
        final Set<Parameter> givenParameters = Set.of(
                Parameter.builder().id(1L).build(),
                Parameter.builder().id(2L).build(),
                Parameter.builder().id(3L).build()
        );
        try (
                final var ignored = mockComponentParserConstruction(
                        Optional.of(givenDate),
                        Optional.of(givenTime),
                        OptionalDouble.of(givenLatitude),
                        OptionalDouble.of(givenLongitude),
                        OptionalDouble.of(givenSpeed),
                        OptionalInt.of(givenCourse),
                        OptionalInt.of(givenAltitude),
                        OptionalInt.of(givenSatelliteCount),
                        OptionalDouble.of(givenHdop),
                        OptionalInt.of(givenInputs),
                        OptionalInt.of(givenOutputs),
                        givenAnalogInputs,
                        Optional.of(givenDriverKeyCode),
                        givenParameters,
                        givenSource
                )
        ) {
            final WialonLocation actual = parser.parse(givenSource);
            final WialonLocation expected = new WialonLocation(
                    givenDate,
                    givenTime,
                    givenLatitude,
                    givenLongitude,
                    givenCourse,
                    givenSpeed,
                    givenAltitude,
                    givenSatelliteCount,
                    givenHdop,
                    givenInputs,
                    givenOutputs,
                    givenAnalogInputs,
                    givenDriverKeyCode,
                    givenParameters
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void locationWithNotDefinedComponentsShouldBeParsed() {
        final String givenSource = "source";
        final double[] givenAnalogInputs = {};
        final Set<Parameter> givenParameters = emptySet();
        try (
                final var ignored = mockComponentParserConstruction(
                        Optional.empty(),
                        Optional.empty(),
                        OptionalDouble.empty(),
                        OptionalDouble.empty(),
                        OptionalDouble.empty(),
                        OptionalInt.empty(),
                        OptionalInt.empty(),
                        OptionalInt.empty(),
                        OptionalDouble.empty(),
                        OptionalInt.empty(),
                        OptionalInt.empty(),
                        givenAnalogInputs,
                        Optional.empty(),
                        givenParameters,
                        givenSource
                )
        ) {
            final WialonLocation actual = parser.parse(givenSource);
            final WialonLocation expected = WialonLocation.builder()
                    .analogInputs(givenAnalogInputs)
                    .parameters(givenParameters)
                    .build();
            assertEquals(expected, actual);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private MockedConstruction<WialonLocationComponentParser> mockComponentParserConstruction(final Optional<LocalDate> date,
                                                                                              final Optional<LocalTime> time,
                                                                                              final OptionalDouble latitude,
                                                                                              final OptionalDouble longitude,
                                                                                              final OptionalDouble speed,
                                                                                              final OptionalInt course,
                                                                                              final OptionalInt altitude,
                                                                                              final OptionalInt satelliteCount,
                                                                                              final OptionalDouble hdop,
                                                                                              final OptionalInt inputs,
                                                                                              final OptionalInt outputs,
                                                                                              final double[] analogInputs,
                                                                                              final Optional<String> driverKeyCode,
                                                                                              final Set<Parameter> parameters,
                                                                                              final String expectedSource) {
        return mockConstruction(
                WialonLocationComponentParser.class,
                (parser, context) -> {
                    when(parser.parseDate()).thenReturn(date);
                    when(parser.parseTime()).thenReturn(time);
                    when(parser.parseLatitude()).thenReturn(latitude);
                    when(parser.parseLongitude()).thenReturn(longitude);
                    when(parser.parseSpeed()).thenReturn(speed);
                    when(parser.parseCourse()).thenReturn(course);
                    when(parser.parseAltitude()).thenReturn(altitude);
                    when(parser.parseSatelliteCount()).thenReturn(satelliteCount);
                    when(parser.parseHdop()).thenReturn(hdop);
                    when(parser.parseInputs()).thenReturn(inputs);
                    when(parser.parseOutputs()).thenReturn(outputs);
                    when(parser.parseAnalogInputs()).thenReturn(analogInputs);
                    when(parser.parseDriverKeyCode()).thenReturn(driverKeyCode);
                    when(parser.parseParameters()).thenReturn(parameters);
                    assertEquals(List.of(expectedSource), context.arguments());
                }
        );
    }
}
