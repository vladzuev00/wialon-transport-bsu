package by.vladzuev.locationreceiver.protocol.apel.decoder.location;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentStateRequestPackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelCurrentStateRequestPackageDecoderTest {
    private final ApelCurrentStateRequestPackageDecoder decoder = new ApelCurrentStateRequestPackageDecoder();

    @Test
    public void speedShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("10"));

        final short actual = decoder.readSpeed(givenBuffer);
        final short expected = 16;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void hdopShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("12"));

        final Byte actual = decoder.readHdop(givenBuffer);
        final Byte expected = 18;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void satelliteCountShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("13"));

        final Byte actual = decoder.readSatelliteCount(givenBuffer);
        final Byte expected = 19;
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void analogInputsShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000000000000000010101011101210131014101510161017"));

        final double[] actual = decoder.readAnalogInputs(givenBuffer);
        final double[] expected = {4112, 4368, 4624, 4880, 5136, 5392, 5648, 5904};
        assertArrayEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Test
    public void packageShouldBeCreated() {
        final ApelLocation givenLocation = ApelLocation.builder().latitude(5).longitude(6).build();

        final ApelCurrentStateRequestPackage actual = decoder.createPackage(givenLocation);
        final ApelCurrentStateRequestPackage expected = new ApelCurrentStateRequestPackage(givenLocation);
        assertEquals(expected, actual);
    }
}
