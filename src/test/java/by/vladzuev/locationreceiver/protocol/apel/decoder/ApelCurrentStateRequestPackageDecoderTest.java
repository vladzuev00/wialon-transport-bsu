package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentStateRequestPackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelCurrentStateRequestPackageDecoderTest {
    private final ApelCurrentStateRequestPackageDecoder decoder = new ApelCurrentStateRequestPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("46e6a95136b693277f11b41a00172709f2ff03160002b9bc630007000000000000000000000000000000c31071090000880500000000000000000000"));

        final ApelCurrentStateRequestPackage actual = decoder.decodeStartingFromBody(givenBuffer);
        final ApelCurrentStateRequestPackage expected = new ApelCurrentStateRequestPackage(
                new ApelLocation(
                        1370089030,
                        663991862,
                        448008575,
                        (short) 0,
                        (byte) 23,
                        (short) 2343,
                        (short) -14,
                        (byte) 3,
                        new double[]{0, 0, 0, 0, 0, 0, 0, 4291}
                )
        );
        assertEquals(expected, actual);

        final int actualReadableBytes = givenBuffer.readableBytes();
        final int expectedReadableBytes = 4 * Integer.BYTES;
        assertEquals(expectedReadableBytes, actualReadableBytes);
    }
}
