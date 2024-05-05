package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder.RequestFactory;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingLoginPackageDecoderTest {
    private final NewWingLoginPackageDecoder decoder = new NewWingLoginPackageDecoder();

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final short givenImei = 1234;
        when(givenBuffer.readShortLE()).thenReturn(givenImei);

        final RequestFactory requestFactory = decoder.decodeUntilChecksum(givenBuffer);

        final int givenChecksum = 53444546;
        final NewWingRequestPackage actual = requestFactory.create(givenChecksum);
        final NewWingRequestPackage expected = new NewWingLoginPackage(givenChecksum, "00000000000000001234");
        assertEquals(expected, actual);
    }
}
