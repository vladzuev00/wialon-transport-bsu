package by.vladzuev.locationreceiver.protocol.apel.decoder.location;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class ApelCurrentLocationResponsePackageDecoderTest {
    private final ApelCurrentLocationResponsePackageDecoder decoder = new ApelCurrentLocationResponsePackageDecoder();

    @Test
    public void speedShouldBeRead() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("1010"));

        final short actual = decoder.readSpeed(givenBuffer);
        final short expected = 4112;
        assertEquals(expected, actual);
    }

    @Test
    public void hdopShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final Byte actual = decoder.readHdop(givenBuffer);
        assertNull(actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void satelliteCountShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final Byte actual = decoder.readSatelliteCount(givenBuffer);
        assertNull(actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void analogInputsShouldBeRead() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final double[] actual = decoder.readAnalogInputs(givenBuffer);
        final double[] expected = {};
        assertArrayEquals(expected, actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void packageShouldBeCreated() {
        final ApelLocation givenLocation = ApelLocation.builder().latitude(5).longitude(6).build();

        final ApelCurrentLocationResponsePackage actual = decoder.createPackage(givenLocation);
        final ApelCurrentLocationResponsePackage expected = new ApelCurrentLocationResponsePackage(givenLocation);
        assertEquals(expected, actual);
    }
}
