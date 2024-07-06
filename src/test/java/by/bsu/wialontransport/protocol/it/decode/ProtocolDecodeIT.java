package by.bsu.wialontransport.protocol.it.decode;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.TextProtocolDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.assertEquals;

public abstract class ProtocolDecodeIT extends AbstractSpringBootTest {

    protected final void test(final ProtocolDecoder<?> givenDecoder, final byte[] givenBytes, final Package expected) {
        final EmbeddedChannel givenChannel = new EmbeddedChannel(givenDecoder);
        final ByteBuf givenBuffer = wrappedBuffer(givenBytes);
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(expected, actual);
    }

    protected final void test(final TextProtocolDecoder givenDecoder, final String givenContent, final Package expected) {
        final byte[] givenBytes = givenContent.getBytes(US_ASCII);
        test(givenDecoder, givenBytes, expected);
    }
}
