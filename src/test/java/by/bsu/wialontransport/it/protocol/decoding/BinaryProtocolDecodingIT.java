package by.bsu.wialontransport.it.protocol.decoding;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.stream.Stream;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@RequiredArgsConstructor
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public abstract class BinaryProtocolDecodingIT extends AbstractSpringBootTest {
    private final BinaryProtocolDecoder decoder;

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

    private void testDecoding(final TestArgument argument, final EmbeddedChannel givenChannel) {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump(argument.givenHexDump));
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(argument.expectedPackage, actual);
    }

    @Value
    protected static class TestArgument {
        String givenHexDump;
        Object expectedPackage;
    }
}
