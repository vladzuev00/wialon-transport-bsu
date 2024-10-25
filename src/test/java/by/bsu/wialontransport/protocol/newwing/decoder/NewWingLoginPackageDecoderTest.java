package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NewWingLoginPackageDecoderTest {
    private final NewWingLoginPackageDecoder decoder = new NewWingLoginPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("c10b"));

        final NewWingLoginPackage actual = decoder.decodeInternal(givenBuffer);
        final NewWingLoginPackage expected = new NewWingLoginPackage((short) 3009);
        assertEquals(expected, actual);
    }
}
