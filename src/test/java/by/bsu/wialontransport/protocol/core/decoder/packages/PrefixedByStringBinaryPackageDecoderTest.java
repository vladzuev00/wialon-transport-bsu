package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedByStringBinaryPackageDecoderTest {
    private final TestPrefixedByStringBinaryPackageDecoder decoder = new TestPrefixedByStringBinaryPackageDecoder();

    @Test
    public void prefixLengthShouldBeGot() {
        final String givenPrefix = "#PREFIX#";

        final int actual = decoder.getLength(givenPrefix);
        final int expected = 8;
        assertEquals(expected, actual);
    }

    @Test
    public void prefixShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("23505245464958233434"));
        final int givenLength = 8;

        final String actual = decoder.readPrefix(givenBuffer, givenLength);
        final String expected = "#PREFIX#";
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readerIndex());
    }

    @Test
    public void prefixesShouldBeEqual() {
        final String givenFirst = "#PREFIX#";
        final String givenSecond = "#PREFIX#";

        assertTrue(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        final String givenFirst = "#PREFIX#";
        final String givenSecond = "#PREFI#";

        assertFalse(decoder.isEqual(givenFirst, givenSecond));
    }

    private static final class TestPrefixedByStringBinaryPackageDecoder extends PrefixedByStringBinaryPackageDecoder {

        public TestPrefixedByStringBinaryPackageDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }
    }
}
