//package by.bsu.wialontransport.protocol.core.decoder.packages;
//
//import io.netty.buffer.ByteBuf;
//import lombok.Value;
//import org.junit.jupiter.api.Test;
//
//import static io.netty.buffer.ByteBufUtil.decodeHexDump;
//import static io.netty.buffer.Unpooled.wrappedBuffer;
//import static java.nio.charset.StandardCharsets.US_ASCII;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public final class FixPrefixedPackageDecoderTest {
//    private final TestFixPrefixedPackageDecoder decoder = new TestFixPrefixedPackageDecoder();
//
//    @Test
//    public void sourceShouldBeDecoded() {
//        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("0064746573742d6d657373616765"));
//
//        final Object actual = decoder.decode(givenBuffer);
//        final Object expected = new TestPackage("test-message");
//        assertEquals(expected, actual);
//    }
//
//    @Value
//    private static class TestPackage {
//        String message;
//    }
//
//    private static final class TestFixPrefixedPackageDecoder extends FixPrefixedPackageDecoder<ByteBuf, Short> {
//        private static final short PREFIX = 100;
//        private static final int MESSAGE_BYTE_COUNT = 12;
//
//        public TestFixPrefixedPackageDecoder() {
//            super(PREFIX);
//        }
//
//        @Override
//        protected int getLength(final Short prefix) {
//            return Short.BYTES;
//        }
//
//        @Override
//        protected ByteBuf skip(final ByteBuf buffer, final int length) {
//            return buffer.skipBytes(length);
//        }
//
//        @Override
//        protected Object decodeInternal(final ByteBuf buffer) {
//            final String message = readMessage(buffer);
//            return new TestPackage(message);
//        }
//
//        @Override
//        protected Short readPrefix(final ByteBuf buffer) {
//            throw new UnsupportedOperationException();
//        }
//
//        private String readMessage(final ByteBuf buffer) {
//            return buffer.readCharSequence(MESSAGE_BYTE_COUNT, US_ASCII).toString();
//        }
//    }
//}
