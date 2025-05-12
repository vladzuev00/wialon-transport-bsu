package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class ApelShortLocationDecoderTest {
    private final ApelShortLocationDecoder decoder = new ApelShortLocationDecoder();

    @Test
    public void speedShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("1012"));

        final short actual = decoder.readSpeed(givenBuffer);
        final short expected = 4624;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void hdopShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final Byte actual = decoder.readHdop(givenBuffer);
        assertNull(actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void satelliteCountShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final Byte actual = decoder.readSatelliteCount(givenBuffer);
        assertNull(actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void analogInputsShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final double[] actual = decoder.readAnalogInputs(givenBuffer);
        final double[] expected = {};
        assertArrayEquals(expected, actual);

        verifyNoInteractions(givenBuffer);
    }
}
