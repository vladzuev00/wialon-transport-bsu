//package by.bsu.wialontransport.protocol.wialon.decoder;
//
//import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.ToString;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public final class WialonPackageDecoderTest {
//    private final TestWialonPackageDecoder decoder = new TestWialonPackageDecoder();
//
//    @Test
//    public void contentShouldBeDecoded() {
//        final String givenContent = "message\r\n";
//
//        final WialonPackage actual = decoder.decodeInternal(givenContent);
//        final TestPackage expected = new TestPackage("message");
//        assertEquals(expected, actual);
//    }
//
//    @RequiredArgsConstructor
//    @Getter
//    @EqualsAndHashCode(callSuper = true)
//    @ToString(callSuper = true)
//    private static final class TestPackage extends WialonPackage {
//        private final String message;
//
//        @Override
//        public String getPrefix() {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    private static final class TestWialonPackageDecoder extends WialonPackageDecoder {
//        private static final String PREFIX = "#PREFIX#";
//
//        public TestWialonPackageDecoder() {
//            super(PREFIX);
//        }
//
//        @Override
//        protected WialonPackage decodeMessage(final String message) {
//            return new TestPackage(message);
//        }
//    }
//}
