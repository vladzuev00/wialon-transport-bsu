//package by.bsu.wialontransport.protocol.core.decoder;
//
//import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ProtocolDecoderTest {
//
//    @Mock
//    private PackageDecoder firstMockedPackageDecoder;
//
//    @Mock
//    private PackageDecoder secondMockedPackageDecoder;
//
//    @Mock
//    private PackageDecoder thirdMockedPackageDecoder;
//
//    private ProtocolDecoder protocolDecoder;
//
//    @Before
//    public void initializeProtocolDecoder() {
//        protocolDecoder = new ProtocolDecoder(
//                List.of(
//                        firstMockedPackageDecoder,
//                        secondMockedPackageDecoder,
//                        thirdMockedPackageDecoder
//                )
//        );
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void bufferShouldBeDecoded() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//        final List<Object> givenOut = mock(List.class);
//
//        when(firstMockedPackageDecoder.isAbleToDecode(same(givenBuffer))).thenReturn(false);
//        when(secondMockedPackageDecoder.isAbleToDecode(same(givenBuffer))).thenReturn(true);
//
//        final Package givenPackage = mock(Package.class);
//        when(secondMockedPackageDecoder.decode(same(givenBuffer))).thenReturn(givenPackage);
//
//        protocolDecoder.decode(givenContext, givenBuffer, givenOut);
//
//        verify(givenBuffer, times(1)).retain();
//        verify(givenOut, times(1)).add(same(givenPackage));
//        verify(givenBuffer, times(1)).release();
//        verify(firstMockedPackageDecoder, times(0)).decode(any(ByteBuf.class));
//        verifyNoInteractions(thirdMockedPackageDecoder);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test(expected = IllegalStateException.class)
//    public void bufferShouldNotBeDecoded() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//        final List<Object> givenOut = mock(List.class);
//
//        when(firstMockedPackageDecoder.isAbleToDecode(same(givenBuffer))).thenReturn(false);
//        when(secondMockedPackageDecoder.isAbleToDecode(same(givenBuffer))).thenReturn(false);
//        when(thirdMockedPackageDecoder.isAbleToDecode(same(givenBuffer))).thenReturn(false);
//
//        protocolDecoder.decode(givenContext, givenBuffer, givenOut);
//    }
//}
