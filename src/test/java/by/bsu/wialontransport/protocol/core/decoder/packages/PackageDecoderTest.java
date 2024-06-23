//package by.bsu.wialontransport.protocol.core.decoder.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import org.junit.Test;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public final class PackageDecoderTest {
//    private static final String GIVEN_PREFIX = "#TEST#";
//
//    private final PackageDecoder<String, Object> decoder = new PackageDecoder<>(GIVEN_PREFIX) {
//
//        @Override
//        public Package decode(final Object source) {
//            throw new RuntimeException();
//        }
//    };
//
//    @Test
//    public void decoderShouldBeAbleToDecode() {
//        final String givenPrefix = "#TEST#";
//
//        final boolean actual = decoder.isAbleToDecode(givenPrefix);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void decoderShouldNotBeAbleToDecode() {
//        final String givenPrefix = "#tEST#";
//
//        final boolean actual = decoder.isAbleToDecode(givenPrefix);
//        assertFalse(actual);
//    }
//}
