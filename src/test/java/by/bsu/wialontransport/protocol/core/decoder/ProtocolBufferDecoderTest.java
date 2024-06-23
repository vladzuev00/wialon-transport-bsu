//package by.bsu.wialontransport.protocol.core.decoder;
//
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.mock;
//
//public final class ProtocolBufferDecoderTest {
//    private final ProtocolBufferDecoder<?> decoder = new ProtocolBufferDecoder<>(null) {
//
//        @Override
//        protected Object getPrefix(final ByteBuf buffer) {
//            throw new UnsupportedOperationException();
//        }
//    };
//
//    @Test
//    public void sourceShouldBeCreated() {
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//
//        final ByteBuf actual = decoder.createSource(givenBuffer);
//        assertSame(givenBuffer, actual);
//    }
//}
