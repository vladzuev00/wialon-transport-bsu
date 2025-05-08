package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelCurrentLocationResponsePackageDecoderTest {
    private final ApelCurrentLocationResponsePackageDecoder decoder = new ApelCurrentLocationResponsePackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("46e6a95136b693277f11b41a00002709f2ff00000000"));

        final ApelCurrentLocationResponsePackage actual = decoder.decodeStartingFromBody(givenBuffer);
        final ApelCurrentLocationResponsePackage expected = new ApelCurrentLocationResponsePackage(
                ApelLocation.builder()
                        .epochSeconds(1370089030)
                        .latitude(663991862)
                        .longitude(448008575)
                        .speed((short) 0)
                        .course((short) 2343)
                        .altitude((short) -14)
                        .build()
        );
        assertEquals(expected, actual);
        assertEquals(Integer.BYTES, givenBuffer.readableBytes());
    }
}
