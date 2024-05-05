package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder.RequestFactory;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingEventCountPackageDecoderTest {
    private final NewWingEventCountPackageDecoder decoder = new NewWingEventCountPackageDecoder();

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final short givenEventCount = 20;
        final short givenFrameEventCount = 5;
        when(givenBuffer.readShortLE())
                .thenReturn(givenEventCount)
                .thenReturn(givenFrameEventCount);

        final RequestFactory requestFactory = decoder.decodeUntilChecksum(givenBuffer);

        final int givenChecksum = 53444546;
        final NewWingRequestPackage actual = requestFactory.create(givenChecksum);
        final var expected = new NewWingEventCountPackage(givenChecksum, givenEventCount, givenFrameEventCount);
        assertEquals(expected, actual);
    }
}
