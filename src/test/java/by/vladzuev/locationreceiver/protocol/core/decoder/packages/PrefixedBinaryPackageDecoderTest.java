package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.lang.Short.MIN_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PrefixedBinaryPackageDecoderTest {
    private final TestPrefixedBinaryPackageDecoder decoder = new TestPrefixedBinaryPackageDecoder();

    @Test
    public void bufferShouldBeSkipped() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052454649586753"));
        final int givenLength = 3;

        decoder.skip(givenBuffer, givenLength);

        final int actual = givenBuffer.readerIndex();
        assertEquals(givenLength, actual);
    }

    private static final class TestPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<Short> {

        public TestPrefixedBinaryPackageDecoder() {
            super(MIN_VALUE);
        }

        @Override
        protected int getLength(final Short prefix) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Short readPrefix(final ByteBuf buffer, final int length) {
            return buffer.readShort();
        }

        @Override
        protected boolean isEqual(final Short first, final Short second) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
