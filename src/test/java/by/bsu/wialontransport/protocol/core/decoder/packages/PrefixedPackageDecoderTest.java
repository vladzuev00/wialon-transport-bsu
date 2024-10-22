package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import java.util.Objects;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PrefixedPackageDecoderTest {
    private final TestPrefixedPackageDecoder decoder = new TestPrefixedPackageDecoder();

    @Test
    public void decoderShouldBeAbleToDecodeBuffer() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052454649586753"));

        assertTrue(decoder.isAbleDecode(givenBuffer));
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeBuffer() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("5052444649586753"));

        assertFalse(decoder.isAbleDecode(givenBuffer));
    }

    @RequiredArgsConstructor
    private static final class TestPrefixedPackageDecoder extends PrefixedPackageDecoder<ByteBuf, String> {
        private static final String REQUIRED_PREFIX = "PREFIX";

        @Override
        public Package decode(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String readPrefix(final ByteBuf buffer) {
            return buffer.readCharSequence(REQUIRED_PREFIX.length(), US_ASCII).toString();
        }

        @Override
        protected boolean isSuitablePrefix(final String prefix) {
            return Objects.equals(prefix, REQUIRED_PREFIX);
        }
    }
}
