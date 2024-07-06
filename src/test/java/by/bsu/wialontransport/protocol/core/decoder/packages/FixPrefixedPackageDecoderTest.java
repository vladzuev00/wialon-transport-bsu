package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.*;

public final class FixPrefixedPackageDecoderTest {
    private final TestFixPrefixedPackageDecoder decoder = new TestFixPrefixedPackageDecoder();

    @Test
    public void sourceShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{0, 100, 109, 101, 115, 115, 97, 103, 101});

        final Package actual = decoder.decode(givenBuffer);
        final TestPackage expected = new TestPackage("message");
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeSuitable() {
        final Short givenPrefix = 100;

        final boolean actual = decoder.isSuitablePrefix(givenPrefix);
        assertTrue(actual);
    }

    @Test
    public void prefixShouldNotBeSuitable() {
        final Short givenPrefix = 101;

        final boolean actual = decoder.isSuitablePrefix(givenPrefix);
        assertFalse(actual);
    }

    @Value
    private static class TestPackage implements Package {
        String message;
    }

    private static final class TestFixPrefixedPackageDecoder extends FixPrefixedPackageDecoder<ByteBuf, Short> {
        private static final Short PREFIX = 100;

        public TestFixPrefixedPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected ByteBuf removePrefix(final ByteBuf buffer) {
            return buffer.skipBytes(2);
        }

        @Override
        protected TestPackage decodeWithoutPrefix(final ByteBuf buffer) {
            final String message = decodeMessage(buffer);
            return new TestPackage(message);
        }

        @Override
        protected Short readPrefix(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        private String decodeMessage(final ByteBuf buffer) {
            return buffer.readCharSequence(buffer.readableBytes(), US_ASCII).toString();
        }
    }
}
