package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PrefixedByBytesBinaryPackageDecoderTest {
    private final TestPrefixedByBytesBinaryPackageDecoder decoder = new TestPrefixedByBytesBinaryPackageDecoder();

    @Test
    public void requiredPrefixLengthShouldBeGot() {
        final byte[] givenRequiredPrefix = {1, 2, 3};

        final int actual = decoder.getLength(givenRequiredPrefix);
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
