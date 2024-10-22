package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class PrefixedPackageDecoderTest {

    @Test
    public void decoderShouldBeAbleToDecodeBuffer() {
        final String givenSuitablePrefix = "PREFIX";
        final String givenReadPrefix = "PREFIX";
        final var givenDecoder = new TestPrefixedPackageDecoder(givenSuitablePrefix, givenReadPrefix);
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final boolean actual = givenDecoder.isAbleDecode(givenBuffer);
        assertTrue(actual);
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeBuffer() {
        final String givenSuitablePrefix = "PREFIX-1";
        final String givenReadPrefix = "PREFIX-2";
        final var givenDecoder = new TestPrefixedPackageDecoder(givenSuitablePrefix, givenReadPrefix);
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final boolean actual = givenDecoder.isAbleDecode(givenBuffer);
        assertFalse(actual);
    }

    @RequiredArgsConstructor
    private static final class TestPrefixedPackageDecoder extends PrefixedPackageDecoder<ByteBuf, String> {
        private final String suitablePrefix;
        private final String readPrefix;

        @Override
        public Package decode(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String readPrefix(final ByteBuf buffer) {
            return readPrefix;
        }

        @Override
        protected boolean isSuitablePrefix(final String prefix) {
            return Objects.equals(prefix, suitablePrefix);
        }
    }
}
