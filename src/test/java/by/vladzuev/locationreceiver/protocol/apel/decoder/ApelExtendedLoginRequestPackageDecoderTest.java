package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.login.ApelExtendedLoginRequestPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelExtendedLoginRequestPackageDecoderTest {
    private final ApelExtendedLoginRequestPackageDecoder decoder = new ApelExtendedLoginRequestPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("f12a00000f003235303032363533343135313036340f0033353638393530333632373938313101002000000000"));

        final ApelExtendedLoginRequestPackage actual = decoder.decodeStartingFromBody(givenBuffer);
        final ApelExtendedLoginRequestPackage expected = new ApelExtendedLoginRequestPackage("356895036279811", " ");
        assertEquals(expected, actual);
        assertEquals(Integer.BYTES, givenBuffer.readableBytes());
    }
}
