//package by.bsu.wialontransport.protocol.core.decoder.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static io.netty.buffer.Unpooled.wrappedBuffer;
//import static java.lang.Short.BYTES;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//
//public final class FixPrefixedBinaryPackageDecoderTest {
//    private final TestFixPrefixedBinaryPackageDecoder decoder = new TestFixPrefixedBinaryPackageDecoder();
//
//    @Test
//    public void prefixShouldBeRead() {
//        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{0, 100, 109, 101, 115, 115, 97, 103, 101});
//
//        final Short actual = decoder.readPrefix(givenBuffer);
//        final Short expected = 100;
//        assertEquals(expected, actual);
//
//        assertEquals(0, givenBuffer.readerIndex());
//    }
//
//    @Test
//    public void prefixShouldBeRemoved() {
//        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{0, 100, 109, 101, 115, 115, 97, 103, 101});
//
//        final ByteBuf actual = decoder.skip(givenBuffer);
//        assertSame(givenBuffer, actual);
//
//        assertEquals(2, givenBuffer.readerIndex());
//    }
//
//    private static final class TestFixPrefixedBinaryPackageDecoder extends FixPrefixedBinaryPackageDecoder<Short> {
//        private static final Short PREFIX = 100;
//
//        public TestFixPrefixedBinaryPackageDecoder() {
//            super(PREFIX);
//        }
//
//        @Override
//        protected Package decodeInternal(final ByteBuf buffer) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        protected int getPrefixByteCount() {
//            return BYTES;
//        }
//
//        @Override
//        protected Short createPrefix(final ByteBuf prefixBytes) {
//            return prefixBytes.readShort();
//        }
//    }
//}
