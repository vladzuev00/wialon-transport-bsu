package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;

public final class FixStringPrefixedBinaryPackageDecoderTest {
    private final TestFixStringPrefixedBinaryPackageDecoder decoder = new TestFixStringPrefixedBinaryPackageDecoder();

    @Test
    public void prefixByteCountShouldBeGot() {
        final int actual = decoder.getPrefixByteCount();
        final int expected = 8;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeCreated() {
        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{35, 80, 82, 69, 70, 73, 88, 35});

        final String actual = decoder.createPrefix(givenBuffer);
        final String expected = "#PREFIX#";
        assertEquals(expected, actual);
    }

    private static final class TestFixStringPrefixedBinaryPackageDecoder extends FixStringPrefixedBinaryPackageDecoder {
        private static final String PREFIX = "#PREFIX#";

        public TestFixStringPrefixedBinaryPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected Package decodeAfterSkipPrefix(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
