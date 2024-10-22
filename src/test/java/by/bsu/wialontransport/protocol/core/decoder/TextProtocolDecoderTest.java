package by.bsu.wialontransport.protocol.core.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TextProtocolDecoderTest {
    private final TextProtocolDecoder decoder = new TextProtocolDecoder(emptyList());

    @Test
    public void sourceShouldBeCreated() {
        final ByteBuf givenBuffer = wrappedBuffer(new byte[]{35, 80, 82, 69, 70, 73, 88, 35, 36, 37, 38});

        final String actual = decoder.createSource(givenBuffer);
        final String expected = "#PREFIX#$%&";
        assertEquals(expected, actual);
    }
}
