package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedByBytesBinaryPackageDecoderTest {
    private final TestPrefixedByBytesBinaryPackageDecoder decoder = new TestPrefixedByBytesBinaryPackageDecoder();

    @Test
    public void prefixLengthShouldBeGot() {
        final byte[] givenPrefix = {1, 2, 3};

        final int actual = decoder.getLength(givenPrefix);
        final int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052454649586753"));
        final int givenLength = 4;

        final byte[] actual = decoder.readPrefix(givenBuffer, givenLength);
        final byte[] expected = {80, 82, 69, 70};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        final byte[] givenFirst = {1, 2, 3};
        final byte[] givenSecond = {1, 2, 3};

        assertTrue(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        final byte[] givenFirst = {1, 2, 3};
        final byte[] givenSecond = {1, 3, 2};

        assertFalse(decoder.isEqual(givenFirst, givenSecond));
    }

    private static final class TestPrefixedByBytesBinaryPackageDecoder extends PrefixedByBytesBinaryPackageDecoder {

        public TestPrefixedByBytesBinaryPackageDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
