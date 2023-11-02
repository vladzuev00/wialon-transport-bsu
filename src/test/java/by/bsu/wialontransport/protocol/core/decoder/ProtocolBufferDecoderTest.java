//package by.bsu.wialontransport.protocol.core.decoder;
//
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.mock;
//
//public final class ProtocolBufferDecoderTest {
//    private final ProtocolBufferDecoder<?, ?> decoder = new ProtocolBufferDecoder<>(null) {
//    };
//
//    @Test
//    public void sourceShouldBeCreated() {
//        final ByteBuf givenByteBuf = mock(ByteBuf.class);
//
//        final ByteBuf actual = this.decoder.createSource(givenByteBuf);
//        assertSame(givenByteBuf, actual);
//    }
//}
