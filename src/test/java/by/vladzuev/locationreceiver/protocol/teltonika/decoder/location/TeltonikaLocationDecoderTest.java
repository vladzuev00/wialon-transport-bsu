package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;

public final class TeltonikaLocationDecoderTest {

    @Test
    public void locationShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump(""));
        throw new UnsupportedOperationException();
    }
}
