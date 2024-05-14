package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;

public final class NewWingDataDecoderTest {
    private final NewWingDataDecoder decoder = new NewWingDataDecoder();

    @Test
    public void nextDataShouldBeDecoded() {
        final byte[] givenBytes = {
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
        };
        final ByteBuf givenBuffer = wrappedBuffer(givenBytes);

        final NewWingData actual = decoder.decodeNext(givenBuffer);
        final NewWingData expected = NewWingData.builder()
                .hour((byte) 18)
                .minute((byte) 51)
                .second((byte) 43)
                .latitudeIntegerPart((short) 5354)
                .latitudeFractionalPart((short) 1978)
                .longitudeIntegerPart((short) 2733)
                .longitudeFractionalPart((short) 1739)
                .hdopIntegerPart((byte) 2)
                .hdopFractionalPart((byte) 123)
                .course((short) 182)
                .speedIntegerPart((short) 0)
                .speedFractionalPart((byte) 10)
                .day((byte) 31)
                .month((byte) 10)
                .year((byte) 23)
                .firstAnalogInputLevel((short) 0)
                .secondAnalogInputLevel((short) 0)
                .thirdAnalogInputLevel((short) 0)
                .fourthAnalogInputLevel((short) 439)
                .flagByte((byte) -22)
                .discreteInputStateByte((byte) 127)
                .checksum((short) 30941)
                .build();
        assertEquals(expected, actual);
    }
}
