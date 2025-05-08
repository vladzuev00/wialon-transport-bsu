package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelRequestCurrentLocationPackageDecoderTest {
    private final ApelRequestCurrentLocationPackageDecoder decoder = new ApelRequestCurrentLocationPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("46e6a95136b693277f11b41a00002709f2ff00000000"));

        final ApelCurrentLocationResponsePackage actual = decoder.decodeStartingFromBody(givenBuffer);
        final ApelCurrentLocationResponsePackage expected = new ApelCurrentLocationResponsePackage(
                new ApelLocation(
                        1370089030,
                        663991862,
                        448008575,
                        (short) 0,
                        (short) 2343,
                        (short) -14
                )
        );
        assertEquals(expected, actual);
        assertEquals(Integer.BYTES, givenBuffer.readableBytes());
    }
}
