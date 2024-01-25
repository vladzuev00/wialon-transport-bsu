package by.bsu.wialontransport.protocol.wialon.encodertemp;

import by.bsu.wialontransport.protocol.wialon.encodertemp.chain.StarterPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WialonEncoderTest {

    @Mock
    private StarterPackageEncoder mockedStarterPackageEncoder;

    @Captor
    private ArgumentCaptor<WialonPackage> packageArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private WialonEncoder encoder;

    @Before
    public void initializeEncoder() {
        this.encoder = new WialonEncoder(this.mockedStarterPackageEncoder);
    }

    @Test
    public void packageShouldBeEncoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final WialonPackage givenPackage = new WialonPackage() {
        };
        final ByteBuf givenByteBuf = mock(ByteBuf.class);

        final String givenEncodedPackage = "#RESPONSE#\r\n";
        when(this.mockedStarterPackageEncoder.encode(any(WialonPackage.class))).thenReturn(givenEncodedPackage);

        this.encoder.encode(givenContext, givenPackage, givenByteBuf);

        verify(this.mockedStarterPackageEncoder, times(1))
                .encode(this.packageArgumentCaptor.capture());
        verify(givenByteBuf, times(1)).writeCharSequence(
                this.stringArgumentCaptor.capture(), any(Charset.class));

        assertSame(givenPackage, this.packageArgumentCaptor.getValue());
        assertEquals(givenEncodedPackage, this.stringArgumentCaptor.getValue());
    }
}
