package by.bsu.wialontransport.protocol.newwing.util;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.protocol.newwing.util.NewWingUtil.*;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NewWingUtilTest {

    @Test
    public void shortShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("0301"));

        final short actual = decodeShort(givenBuffer);
        final short expected = 259;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void imeiShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("0301"));

        final String actual = decodeImei(givenBuffer);
        final String expected = "259";
        assertEquals(expected, actual);
    }

    @Test
    public void dateShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("090807"));

        final LocalDate actual = decodeDate(givenBuffer);
        final LocalDate expected = LocalDate.of(2007, 8, 9);
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void timeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("090807"));

        final LocalTime actual = decodeTime(givenBuffer);
        final LocalTime expected = LocalTime.of(9, 8, 7);
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void latitudeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("ea14ba07"));

        final double actual = decodeLatitude(givenBuffer);
        final double expected = 53.91630172729492;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void longitudeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("ad0acb06"));

        final double actual = decodeLongitude(givenBuffer);
        final double expected = 27.56231689453125;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void hdopShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("027b"));

        final double actual = decodeHdop(givenBuffer);
        final double expected = 2.123;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void speedShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000a"));

        final double actual = decodeSpeed(givenBuffer);
        final double expected = 0.1;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void analogInputsShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("000000000000b701"));

        final double[] actual = decodeAnalogInputs(givenBuffer);
        final double[] expected = {0, 0, 0, 439};
        assertArrayEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }
}
