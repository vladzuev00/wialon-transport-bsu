//package by.bsu.wialontransport.protocol.core.decoder.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static io.netty.buffer.Unpooled.wrappedBuffer;
//import static java.nio.charset.StandardCharsets.US_ASCII;
//import static org.junit.Assert.assertEquals;
//
//public final class FixStringPrefixedPackageDecoderTest {
//    private final TestFixStringPrefixedPackageDecoder decoder = new TestFixStringPrefixedPackageDecoder();
//
//    @Test
//    public void prefixShouldBeRead() {
//        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{35, 80, 82, 69, 70, 73, 88, 35, 36, 37, 38, 39});
//
//        final String actual = decoder.readPrefix(givenBuffer);
//        final String expected = "#PREFIX#";
//        assertEquals(expected, actual);
//    }
//
//    private static final class TestFixStringPrefixedPackageDecoder extends FixStringPrefixedPackageDecoder<ByteBuf> {
//        private static final String PREFIX = "#PREFIX#";
//
//        public TestFixStringPrefixedPackageDecoder() {
//            super(PREFIX);
//        }
//
//        @Override
//        public Package decode(final ByteBuf buffer) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        protected String readPrefix(final ByteBuf buffer, final int length) {
//            return buffer.readCharSequence(length, US_ASCII).toString();
//        }
//    }
//}
