package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelFullLocationDecoderTest {
    private final ApelFullLocationDecoder decoder = new ApelFullLocationDecoder();

    @Test
    public void speedShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("15"));

        final short actual = decoder.readSpeed(givenBuffer);
        final short expected = 21;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void hdopShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("25"));

        final Byte actual = decoder.readHdop(givenBuffer);
        final Byte expected = 37;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void satelliteCountShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("35"));

        final Byte actual = decoder.readSatelliteCount(givenBuffer);
        final Byte expected = 53;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void analogInputsShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000000000000000010111012101310141015101610171018"));

        final double[] actual = decoder.readAnalogInputs(givenBuffer);
        final double[] expected = {4368., 4624., 4880., 5136., 5392., 5648., 5904., 6160.};
        assertArrayEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }
}
