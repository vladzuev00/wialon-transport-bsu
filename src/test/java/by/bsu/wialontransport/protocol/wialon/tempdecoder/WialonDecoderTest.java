package by.bsu.wialontransport.protocol.wialon.tempdecoder;

import by.bsu.wialontransport.protocol.wialon.tempdecoder.chain.StarterPackageDecoder;
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

import java.util.ArrayList;
import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WialonDecoderTest {

    @Mock
    private StarterPackageDecoder mockedStarterPackageDecoder;

    private WialonDecoder decoder;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializeDecoder() {
        this.decoder = new WialonDecoder(this.mockedStarterPackageDecoder);
    }

    @Test
    public void byteBufferShouldBeDecoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenSerializedPackageWithoutPostfix = "#PREFIX#message";
        final String givenSerializedPackageWithPostfix = givenSerializedPackageWithoutPostfix + "\r\n";
        final ByteBuf givenByteBuf = wrappedBuffer(givenSerializedPackageWithPostfix.getBytes(UTF_8));

        final List<Object> givenOutObjects = new ArrayList<>();
        final WialonPackage givenPackage = new WialonPackage() {
        };

        when(this.mockedStarterPackageDecoder.decode(anyString()))
                .thenReturn(givenPackage);

        this.decoder.decode(givenContext, givenByteBuf, givenOutObjects);

        assertTrue(givenOutObjects.contains(givenPackage));

        verify(this.mockedStarterPackageDecoder, times(1))
                .decode(this.stringArgumentCaptor.capture());
        assertEquals(givenSerializedPackageWithoutPostfix, this.stringArgumentCaptor.getValue());
    }
}
