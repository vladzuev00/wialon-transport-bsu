package by.vladzuev.locationreceiver.protocol.core.encoder;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.PackageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolEncoderTest {

    @Mock
    private PackageEncoder<?> firstMockedPackageEncoder;

    @Mock
    private PackageEncoder<?> secondMockedPackageEncoder;

    @Mock
    private PackageEncoder<?> thirdMockedPackageEncoder;

    private ProtocolEncoder protocolEncoder;

    @BeforeEach
    public void initializeProtocolEncoder() {
        protocolEncoder = new ProtocolEncoder(
                List.of(
                        firstMockedPackageEncoder,
                        secondMockedPackageEncoder,
                        thirdMockedPackageEncoder
                )
        );
    }

    @Test
    public void responseShouldBeEncoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenResponse = new Object();
        final ByteBuf givenOut = mock(ByteBuf.class);

        when(firstMockedPackageEncoder.isAbleEncode(same(givenResponse))).thenReturn(false);
        when(secondMockedPackageEncoder.isAbleEncode(same(givenResponse))).thenReturn(true);

        final byte[] givenResponseBytes = {1, 2, 3, 4, 5};
        when(secondMockedPackageEncoder.encode(same(givenResponse))).thenReturn(givenResponseBytes);

        protocolEncoder.encode(givenContext, givenResponse, givenOut);

        verify(givenOut, times(1)).writeBytes(same(givenResponseBytes));
        verify(firstMockedPackageEncoder, times(0)).encode(any());
        verifyNoInteractions(thirdMockedPackageEncoder, givenContext);
    }

    @Test
    public void responseShouldNotBeEncoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenResponse = new Object();
        final ByteBuf givenOut = mock(ByteBuf.class);

        when(firstMockedPackageEncoder.isAbleEncode(same(givenResponse))).thenReturn(false);
        when(secondMockedPackageEncoder.isAbleEncode(same(givenResponse))).thenReturn(false);
        when(thirdMockedPackageEncoder.isAbleEncode(same(givenResponse))).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> protocolEncoder.encode(givenContext, givenResponse, givenOut));

        verify(firstMockedPackageEncoder, times(0)).encode(any());
        verify(secondMockedPackageEncoder, times(0)).encode(any());
        verify(thirdMockedPackageEncoder, times(0)).encode(any());
        verifyNoInteractions(givenContext, givenOut);
    }
}
