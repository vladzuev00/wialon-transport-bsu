//package by.bsu.wialontransport.protocol.core.decoder.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public final class FixStringPrefixedTextPackageDecoderTest {
//    private final TestFixStringPrefixedTextPackageDecoder decoder = new TestFixStringPrefixedTextPackageDecoder();
//
//    @Test
//    public void prefixShouldBeRead() {
//        final String givenContent = "#PREFIX#message\r\n";
//
//        final String actual = decoder.readPrefix(givenContent);
//        final String expected = "#PREFIX#";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void prefixShouldBeRemoved() {
//        final String givenContent = "#PREFIX#message\r\n";
//
//        final String actual = decoder.skip(givenContent);
//        final String expected = "message\r\n";
//        assertEquals(expected, actual);
//    }
//
//    private static final class TestFixStringPrefixedTextPackageDecoder extends FixStringPrefixedTextPackageDecoder {
//        private static final String PREFIX = "#PREFIX#";
//
//        public TestFixStringPrefixedTextPackageDecoder() {
//            super(PREFIX);
//        }
//
//        @Override
//        protected Package decodeInternal(final String content) {
//            throw new UnsupportedOperationException();
//        }
//    }
//}
