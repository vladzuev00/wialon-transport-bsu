package by.bsu.wialontransport.protocol.it.decode;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;

public abstract class ProtocolDecodeIT extends AbstractSpringBootTest {

    protected final void test(final ProtocolDecoder<?> givenDecoder, final byte[] givenBytes, final Package expected) {
        final EmbeddedChannel givenChannel = new EmbeddedChannel(givenDecoder);
        final ByteBuf givenBuffer = wrappedBuffer(givenBytes);
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(expected, actual);
    }
}
