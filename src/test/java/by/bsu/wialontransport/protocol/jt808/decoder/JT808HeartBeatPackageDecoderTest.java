package by.bsu.wialontransport.protocol.jt808.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public final class JT808HeartBeatPackageDecoderTest {
    private final JT808HeartBeatPackageDecoder decoder = new JT808HeartBeatPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final String givenPhoneNumber = "";

        final Object actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
        assertNotNull(actual);
    }
}
