package by.bsu.wialontransport.protocol.core.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class BinaryProtocolDecoderTest {
    private final BinaryProtocolDecoder decoder = new BinaryProtocolDecoder(emptyList());

    @Test
    public void sourceShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final ByteBuf actual = decoder.createSource(givenBuffer);
        assertSame(givenBuffer, actual);
    }
}