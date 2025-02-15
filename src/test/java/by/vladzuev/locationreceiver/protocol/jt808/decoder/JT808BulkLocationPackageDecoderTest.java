package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JT808BulkLocationPackageDecoderTest {
    private final JT808BulkLocationPackageDecoder decoder = new JT808BulkLocationPackageDecoder();

    @Test
    public void locationCountShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("070434"));

        final int actual = decoder.decodeLocationCount(givenBuffer);
        final int expected = 1796;
        assertEquals(expected, actual);

        final int actualReaderIndex = givenBuffer.readerIndex();
        final int expectedReaderIndex = Short.BYTES;
        assertEquals(expectedReaderIndex, actualReaderIndex);
    }

    @Test
    public void bufferShouldBeSkippedUntilFirstLocation() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("070434"));

        decoder.skipUntilFirstLocation(givenBuffer);

        final int actual = givenBuffer.readerIndex();
        final int expected = Byte.BYTES;
        assertEquals(expected, actual);
    }

    @Test
    public void locationPrefixShouldBeSkipped() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("070434"));

        decoder.skipLocationPrefix(givenBuffer);

        final int actual = givenBuffer.readerIndex();
        final int expected = Short.BYTES;
        assertEquals(expected, actual);
    }
}
