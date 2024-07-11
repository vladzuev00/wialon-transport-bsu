package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator.NewWingLatitudeCalculator;
import by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator.NewWingLongitudeCalculator;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataDecoderTest extends AbstractSpringBootTest {

    @Mock
    private NewWingLatitudeCalculator mockedLatitudeCalculator;

    @Mock
    private NewWingLongitudeCalculator mockedLongitudeCalculator;

    private NewWingDataDecoder decoder;

    @Before
    public void initializeDecoder() {
        decoder = new NewWingDataDecoder(mockedLatitudeCalculator, mockedLongitudeCalculator);
    }

    @Test
    public void nextDataShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(
                new byte[]{
                        18,             //HOURS
                        51,             //MINUTES
                        43,             //SECONDS
                        -22, 20,        //LATITUDE_INTEGER_PART
                        -70, 7,         //LATITUDE_FRACTIONAL_PART
                        -83, 10,        //LONGITUDE_INTEGER_PART
                        -53, 6,         //LONGITUDE_INTEGER_PART
                        2,              //HDOP_INTEGER_PART
                        123,            //HDOP_FRACTIONAL_PART
                        -74, 0,         //COURSE
                        0, 0,           //SPEED_INTEGER_PART
                        10,             //SPEED_FRACTIONAL_PART
                        31,             //DAY
                        10,             //MONTH
                        23,             //YEAR
                        0, 0,           //FIRST_ANALOG_INPUT_LEVEL
                        0, 0,           //SECOND_ANALOG_INPUT_LEVEL
                        0, 0,           //THIRD_ANALOG_INPUT_LEVEL
                        -73, 1,         //FOURTH_ANALOG_INPUT_LEVEL
                        -22,            //FLAGS_BYTE
                        127,            //DISCRETE_INPUT_STATE
                        -35, 120        //CHECKSUM
                }
        );

        final short expectedLatitudeIntegerPart = 5354;
        final short expectedLatitudeFractionalPart = 1978;
        final double givenLatitude = 53.454545;
        when(mockedLatitudeCalculator.calculate(eq(expectedLatitudeIntegerPart), eq(expectedLatitudeFractionalPart)))
                .thenReturn(givenLatitude);

        final short expectedLongitudeIntegerPart = 2733;
        final short expectedLongitudeFractionalPart = 1739;
        final double givenLongitude = 23.54354;
        when(mockedLongitudeCalculator.calculate(eq(expectedLongitudeIntegerPart), eq(expectedLongitudeFractionalPart)))
                .thenReturn(givenLongitude);

        final Data actual = decoder.decodeNext(givenBuffer);
        final Data expected = Data.builder()
                .dateTime(LocalDateTime.of(2023, 10, 31, 18, 51, 43))
                .coordinate(new Coordinate(givenLatitude, givenLongitude))
                .course(182)
                .speed(0.10)
                .hdop(2.123)
                .analogInputs(new double[]{0, 0, 0, 439})
                .build();
        assertEquals(expected, actual);
    }
}
