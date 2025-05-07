package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedByUnsignedShortLEBinaryPackageDecoderTest {
    private final TestDecoder decoder = new TestDecoder();

    @Test
    public void prefixLengthShouldBeGot() {
        final Integer givenPrefix = 255;

        final int actual = decoder.getLength(givenPrefix);
        final int expected = Short.BYTES;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("0c002900f12a00000f003235303032363533343135313036340f0033353638393530333632373938313101002000000000"));
        final int givenLength = Short.BYTES;

        final int actual = decoder.readPrefix(givenBuffer, givenLength);
        final int expected = 12;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readerIndex());
    }

    @Test
    public void prefixesShouldBeEqual() {
        final Integer givenFirst = 255;
        final Integer givenSecond = 255;

        assertTrue(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        final Integer givenFirst = 255;
        final Integer givenSecond = 256;

        assertFalse(decoder.isEqual(givenFirst, givenSecond));
    }

    private static final class TestDecoder extends PrefixedByUnsignedShortLEBinaryPackageDecoder {

        public TestDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
