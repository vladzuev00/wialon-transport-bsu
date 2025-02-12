package by.bsu.wialontransport.it.protocol.decoding;

import by.bsu.wialontransport.base.AbstractSpringBootIT;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public abstract class ProtocolDecodingIT extends AbstractSpringBootIT {
    private final ProtocolDecoder<?> decoder;

    @Test
    public final void packagesShouldBeDecoded() {
        final EmbeddedChannel givenChannel = new EmbeddedChannel(decoder);
        try {
            provideArguments().forEach(argument -> testDecoding(argument, givenChannel));
        } finally {
            givenChannel.close();
        }
    }

    protected abstract Stream<TestArgument> provideArguments();

    protected abstract byte[] getBytes(final String source);

    private void testDecoding(final TestArgument givenArgument, final EmbeddedChannel givenChannel) {
        final ByteBuf givenBuffer = wrappedBuffer(getBytes(givenArgument.givenSource));
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(givenArgument.expectedPackage, actual);
    }

    @Value
    protected static class TestArgument {
        String givenSource;
        Object expectedPackage;
    }
}
