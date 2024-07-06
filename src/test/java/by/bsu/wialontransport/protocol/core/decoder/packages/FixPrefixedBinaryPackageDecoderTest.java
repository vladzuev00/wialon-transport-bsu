package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.lang.Short.BYTES;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.assertEquals;

public final class FixPrefixedBinaryPackageDecoderTest {
    private final TestFixPrefixedBinaryPackageDecoder decoder = new TestFixPrefixedBinaryPackageDecoder();

    @Test
    public void bufferShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{0, 100, 109, 101, 115, 115, 97, 103, 101});

        final Package actual = decoder.decode(givenBuffer);
        final TestPackage expected = new TestPackage("message");
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void prefixShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{0, 100, 109, 101, 115, 115, 97, 103, 101});

        final Short actual = decoder.readPrefix(givenBuffer);
        final Short expected = 100;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readerIndex());
    }

    @Value
    private static class TestPackage implements Package {
        String text;
    }

    private static final class TestFixPrefixedBinaryPackageDecoder extends FixPrefixedBinaryPackageDecoder<Short> {
        private static final Short PREFIX = 100;

        public TestFixPrefixedBinaryPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected int getPrefixByteCount() {
            return BYTES;
        }

        @Override
        protected TestPackage decodeWithoutPrefix(final ByteBuf buffer) {
            final String message = decodeMessage(buffer);
            return new TestPackage(message);
        }

        @Override
        protected Short createPrefix(final ByteBuf prefixBytes) {
            return prefixBytes.readShort();
        }

        private String decodeMessage(final ByteBuf buffer) {
            return buffer.readCharSequence(buffer.readableBytes(), US_ASCII).toString();
        }
    }
}
