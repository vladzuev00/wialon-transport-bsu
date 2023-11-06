package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.builder.LoginNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.mockito.Mockito.*;

public final class LoginNewWingPackageDecoderTest {
    private final LoginNewWingPackageDecoder decoder = new LoginNewWingPackageDecoder();

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final LoginNewWingPackageBuilder givenPackageBuilder = mock(LoginNewWingPackageBuilder.class);

        final short givenImei = 1234;
        when(givenBuffer.readShortLE()).thenReturn(givenImei);

        this.decoder.decodeUntilChecksum(givenBuffer, givenPackageBuilder);

        final String expectedAccumulatedImei = "00000000000000001234";
        verify(givenPackageBuilder, times(1)).setImei(expectedAccumulatedImei);
    }
}
