package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class PrefixedByShortLEBinaryPackageDecoderTest {
    private final TestDecoder decoder = new TestDecoder();

    @Test
    public void prefixLengthShouldBeGot() {
        final Short givenPrefix = 255;

        final int actual = decoder.getLength(givenPrefix);
        final int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final int givenLength = Short.BYTES;

        final short givenPrefix = 255;
        when(givenBuffer.readShortLE()).thenReturn(givenPrefix);

        final short actual = decoder.readPrefix(givenBuffer, givenLength);
        assertEquals(givenPrefix, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        final Short givenFirst = 255;
        final Short givenSecond = 255;

        assertTrue(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        final Short givenFirst = 255;
        final Short givenSecond = 256;

        assertFalse(decoder.isEqual(givenFirst, givenSecond));
    }

    private static final class TestDecoder extends PrefixedByShortLEBinaryPackageDecoder {

        public TestDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
