package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TeltonikaLoginPackageDecoderTest {
    private final TeltonikaLoginPackageDecoder decoder = new TeltonikaLoginPackageDecoder(null);

    @Test
    public void packageShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("313233343536373839303132333435"));

        final TeltonikaRequestLoginPackage actual = decoder.decodeInternal(givenBuffer);
        final TeltonikaRequestLoginPackage expected = new TeltonikaRequestLoginPackage("123456789012345");
        assertEquals(expected, actual);
    }
}
