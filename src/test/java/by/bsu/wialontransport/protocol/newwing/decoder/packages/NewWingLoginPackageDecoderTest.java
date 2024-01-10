package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingLoginPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.mockito.Mockito.*;

public final class NewWingLoginPackageDecoderTest {
    private final NewWingLoginPackageDecoder decoder = new NewWingLoginPackageDecoder();

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final NewWingLoginPackageBuilder givenPackageBuilder = mock(NewWingLoginPackageBuilder.class);

        final short givenImei = 1234;
        when(givenBuffer.readShortLE()).thenReturn(givenImei);

        decoder.decodeUntilChecksum(givenBuffer, givenPackageBuilder);

        final String expectedAccumulatedImei = "00000000000000001234";
        verify(givenPackageBuilder, times(1)).setImei(expectedAccumulatedImei);
    }
}
