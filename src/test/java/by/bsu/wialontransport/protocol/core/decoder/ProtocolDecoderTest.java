package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolDecoderTest {

    @Mock
    private PackageDecoder<ByteBuf> firstMockedPackageDecoder;

    @Mock
    private PackageDecoder<ByteBuf> secondMockedPackageDecoder;

    @Mock
    private PackageDecoder<ByteBuf> thirdMockedPackageDecoder;

    private TestProtocolDecoder protocolDecoder;

    @BeforeEach
    public void initializeProtocolDecoder() {
        protocolDecoder = new TestProtocolDecoder(
                List.of(
                        firstMockedPackageDecoder,
                        secondMockedPackageDecoder,
                        thirdMockedPackageDecoder
                )
        );
    }

    @Test
    public void bufferShouldBeDecoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        @SuppressWarnings("unchecked") final List<Object> givenOut = mock(List.class);

        when(firstMockedPackageDecoder.isAbleDecode(same(givenBuffer))).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleDecode(same(givenBuffer))).thenReturn(true);

        final int givenReadableBytes = 10;
        when(givenBuffer.readableBytes()).thenReturn(givenReadableBytes);

        final Object givenRequest = new Object();
        when(secondMockedPackageDecoder.decode(same(givenBuffer))).thenReturn(givenRequest);

        protocolDecoder.decode(givenContext, givenBuffer, givenOut);

        verify(givenBuffer, times(1)).retain();
        verify(givenOut, times(1)).add(same(givenRequest));
        verify(givenBuffer, times(1)).skipBytes(eq(givenReadableBytes));
        verify(givenBuffer, times(1)).release();
        verify(firstMockedPackageDecoder, times(0)).decode(any(ByteBuf.class));
        verifyNoInteractions(thirdMockedPackageDecoder);
    }

    @Test
    public void bufferShouldNotBeDecoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        @SuppressWarnings("unchecked") final List<Object> givenOut = mock(List.class);

        when(firstMockedPackageDecoder.isAbleDecode(same(givenBuffer))).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleDecode(same(givenBuffer))).thenReturn(false);
        when(thirdMockedPackageDecoder.isAbleDecode(same(givenBuffer))).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> protocolDecoder.decode(givenContext, givenBuffer, givenOut));

        verify(givenBuffer, times(1)).retain();
        verify(givenBuffer, times(1)).release();
        verify(givenBuffer, times(0)).readableBytes();
        verify(givenBuffer, times(0)).skipBytes(anyInt());
        verify(firstMockedPackageDecoder, times(0)).decode(any(ByteBuf.class));
        verify(secondMockedPackageDecoder, times(0)).decode(any(ByteBuf.class));
        verify(thirdMockedPackageDecoder, times(0)).decode(any(ByteBuf.class));
        verifyNoInteractions(givenOut);
    }

    private static final class TestProtocolDecoder extends ProtocolDecoder<ByteBuf> {

        public TestProtocolDecoder(final List<PackageDecoder<ByteBuf>> packageDecoders) {
            super(packageDecoders);
        }

        @Override
        protected ByteBuf createSource(final ByteBuf buffer) {
            return buffer;
        }
    }
}
