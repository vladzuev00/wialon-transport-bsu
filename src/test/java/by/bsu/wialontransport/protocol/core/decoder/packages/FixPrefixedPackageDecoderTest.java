package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.binary.FixPrefixedPackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class FixPrefixedPackageDecoderTest {
    private final TestFixPrefixedPackageDecoder decoder = new TestFixPrefixedPackageDecoder();

    @Test
    public void decoderShouldBeAbleToDecodeBuffer() {
        final String givenPrefix = "PREFIX";

        final boolean actual = decoder.isAbleToDecode(givenPrefix);
        assertTrue(actual);
    }

    @Test
    public void decoderShouldNotBeAbleToDecodeBuffer() {
        final String givenPrefix = "PREFIX-1";

        final boolean actual = decoder.isAbleToDecode(givenPrefix);
        assertFalse(actual);
    }

    private static final class TestFixPrefixedPackageDecoder extends FixPrefixedPackageDecoder<String> {
        private static final String PREFIX = "PREFIX";

        public TestFixPrefixedPackageDecoder() {
            super(PREFIX);
        }

        @Override
        public Package decode(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String readPrefix(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
