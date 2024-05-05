//package by.bsu.wialontransport.protocol.newwing.decoder.packages;
//
//import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingEventCountPackageBuilder;
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static org.mockito.Mockito.*;
//
//public final class NewWingEventCountPackageDecoderTest {
//    private final NewWingEventCountPackageDecoder decoder = new NewWingEventCountPackageDecoder();
//
//    @Test
//    public void bufferShouldBeDecodedUntilChecksum() {
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//        final NewWingEventCountPackageBuilder givenPackageBuilder = mock(NewWingEventCountPackageBuilder.class);
//
//        final short givenEventCount = 20;
//        final short givenFrameEventCount = 5;
//        when(givenBuffer.readShortLE())
//                .thenReturn(givenEventCount)
//                .thenReturn(givenFrameEventCount);
//
//        decoder.decodeUntilChecksum(givenBuffer, givenPackageBuilder);
//
//        verify(givenPackageBuilder, times(1)).setEventCount(eq(givenEventCount));
//        verify(givenPackageBuilder, times(1)).setFrameEventCount(eq(givenFrameEventCount));
//    }
//}
