package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808AuthenticationPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public final class JT808AuthenticationPackageDecoderTest {
    private final JT808AuthenticationPackageDecoder decoder = new JT808AuthenticationPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final String givenPhoneNumber = "";

        final JT808AuthenticationPackage actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
        assertNotNull(actual);
    }
}
