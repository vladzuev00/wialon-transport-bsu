//package by.bsu.wialontransport.protocol.newwing.decoder.packages;
//
//import by.bsu.wialontransport.protocol.newwing.decoder.NewWingPackageDecoder;
//import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public final class NewWingPackageDecoderTest {
//    private static final String GIVEN_PREFIX = "#TEST#";
//
//    private final TestNewWingPackageDecoder decoder = new TestNewWingPackageDecoder(GIVEN_PREFIX);
//
//    @Test
//    public void bufferShouldBeDecoded() {
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//
//        final int givenChecksum = 12345;
//        when(givenBuffer.readIntLE()).thenReturn(givenChecksum);
//
//        final NewWingRequestPackage actual = decoder.decode(givenBuffer);
//        final NewWingRequestPackage expected = new TestPackage(givenChecksum);
//        assertEquals(expected, actual);
//    }
//
//    private static final class TestPackage extends NewWingRequestPackage {
//
//        public TestPackage(final int checksum) {
//            super(checksum);
//        }
//    }
//
//    private static final class TestNewWingPackageDecoder extends NewWingPackageDecoder {
//
//        public TestNewWingPackageDecoder(final String prefix) {
//            super(prefix);
//        }
//
//        @Override
//        protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
//            return TestPackage::new;
//        }
//    }
//}