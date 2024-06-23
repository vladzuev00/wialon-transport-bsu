package by.bsu.wialontransport.protocol.teltonika.decoder.packages;

import by.bsu.wialontransport.protocol.teltonika.model.packages.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.*;

public final class TeltonikaRequestLoginPackageDecoderTest {
    private final TeltonikaRequestLoginPackageDecoder decoder = new TeltonikaRequestLoginPackageDecoder();

    @Test
    public void bufferShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(
                new byte[]{0, 15, 51, 53, 54, 51, 48, 55, 48, 52, 50, 52, 52, 49, 48, 49, 51}
        );

        final TeltonikaRequestLoginPackage actual = decoder.decode(givenBuffer);
        final TeltonikaRequestLoginPackage expected = new TeltonikaRequestLoginPackage("356307042441013");
        assertEquals(expected, actual);

        assertFalse(givenBuffer.isReadable());
    }

    @Test
    public void prefixShouldBeGot() {
        final ByteBuf givenBuffer = wrappedBuffer(
                new byte[]{0, 15, 51, 53, 54, 51, 48, 55, 48, 52, 50, 52, 52, 49, 48, 49, 51}
        );

        final Short actual = decoder.getPrefix(givenBuffer);
        final Short expected = 15;
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readerIndex());
    }

    @Test
    public void decoderShouldBeAbleToDecode() {
        final Short givenPrefix = 1;

        final boolean actual = decoder.isAbleToDecode(givenPrefix);
        assertTrue(actual);
    }

    @Test
    public void decoderShouldNotBeAbleToDecode() {
        final Short givenPrefix = 0;

        final boolean actual = decoder.isAbleToDecode(givenPrefix);
        assertFalse(actual);
    }
}
