package by.bsu.wialontransport.protocol.wialon.decoder.location.parser;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
                final var ignored = mockConstruction(
                        WialonLocationComponentParser.class,
                        (parser, context) -> {
                            when(parser.parseDate()).thenReturn(Optional.of(givenDate));
                            when(parser.parseTime()).thenReturn(Optional.of(givenTime));
                            when(parser.parseLatitude()).thenReturn(OptionalDouble.of(givenLatitude));
                            when(parser.parseLongitude()).thenReturn(OptionalDouble.of(givenLongitude));
                            when(parser.parseSpeed()).thenReturn(OptionalDouble.of(givenSpeed));
                            when(parser.parseCourse()).thenReturn(OptionalInt.of(givenCourse));
                            when(parser.parseAltitude()).thenReturn(OptionalInt.of(givenAltitude));
                            when(parser.parseSatelliteCount()).thenReturn(OptionalInt.of(givenSatelliteCount));
                            when(parser.parseHdop()).thenReturn(OptionalDouble.of(givenHdop));
                            when(parser.parseInputs()).thenReturn(OptionalInt.of(givenInputs));
                            when(parser.parseOutputs()).thenReturn(OptionalInt.of(givenOutputs));
                            when(parser.parseAnalogInputs()).thenReturn(givenAnalogInputs);
                            when(parser.parseDriverKeyCode()).thenReturn(Optional.of(givenDriverKeyCode));
                            when(parser.parseParameters()).thenReturn(givenParameters);
                            assertEquals(List.of(givenSource), context.arguments());
                        }
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

    private MockedConstruction<WialonLocationComponentParser> mockComponentParser(final WialonLocationComponentParser parser,
                                                                                  final LocalDate date,
                                                                                  final LocalTime time,
                                                                                  final double latitude,
                                                                                  final double longitude,
                                                                                  final double speed,
                                                                                  final int course,
                                                                                  final int altitude,
                                                                                  final int satelliteCount,
                                                                                  final double hdop,
                                                                                  final int inputs,
                                                                                  final int outputs,
                                                                                  final String driverKeyCode,
                                                                                  final Set<Parameter> parameters,
                                                                                  final String expectedSource) {
        when(parser.parseDate()).thenReturn(Optional.of(givenDate));
        when(parser.parseTime()).thenReturn(Optional.of(givenTime));
        when(parser.parseLatitude()).thenReturn(OptionalDouble.of(givenLatitude));
        when(parser.parseLongitude()).thenReturn(OptionalDouble.of(givenLongitude));
        when(parser.parseSpeed()).thenReturn(OptionalDouble.of(givenSpeed));
        when(parser.parseCourse()).thenReturn(OptionalInt.of(givenCourse));
        when(parser.parseAltitude()).thenReturn(OptionalInt.of(givenAltitude));
        when(parser.parseSatelliteCount()).thenReturn(OptionalInt.of(givenSatelliteCount));
        when(parser.parseHdop()).thenReturn(OptionalDouble.of(givenHdop));
        when(parser.parseInputs()).thenReturn(OptionalInt.of(givenInputs));
        when(parser.parseOutputs()).thenReturn(OptionalInt.of(givenOutputs));
        when(parser.parseAnalogInputs()).thenReturn(givenAnalogInputs);
        when(parser.parseDriverKeyCode()).thenReturn(Optional.of(givenDriverKeyCode));
        when(parser.parseParameters()).thenReturn(givenParameters);
        assertEquals(List.of(givenSource), context.arguments());
    }
}
