//package by.bsu.wialontransport.protocol.newwing.decoder;
//
//import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
//import by.bsu.wialontransport.protocol.newwing.util.NewWingUtil;
//import io.netty.buffer.ByteBuf;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import static by.bsu.wialontransport.protocol.newwing.util.NewWingUtil.decodeImei;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//
//public final class NewWingLoginPackageDecoderTest {
//    private final NewWingLoginPackageDecoder decoder = new NewWingLoginPackageDecoder();
//
//    @Test
//    public void bufferShouldBeDecodedInternally() {
//        try (final MockedStatic<NewWingUtil> mockedUtil = mockStatic(NewWingUtil.class)) {
//            final ByteBuf givenBuffer = mock(ByteBuf.class);
//
//            final short givenImei = "11112222333344445555";
//            mockedUtil.when(() -> decodeImei(same(givenBuffer))).thenReturn(givenImei);
//
//            final NewWingLoginPackage actual = decoder.decodeInternal(givenBuffer);
//            final NewWingLoginPackage expected = new NewWingLoginPackage(givenImei);
//            assertEquals(expected, actual);
//        }
//    }
//}
