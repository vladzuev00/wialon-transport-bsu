package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808AuthPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public final class JT808AuthPackageDecoderTest {
    private final JT808AuthPackageDecoder decoder = new JT808AuthPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final String givenPhoneNumber = "";

        final JT808AuthPackage actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
        assertNotNull(actual);
    }
}
