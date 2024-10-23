package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808RegistrationMessage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JT808RegistrationPackageDecoderTest {
    private final JT808RegistrationPackageDecoder decoder = new JT808RegistrationPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("000000003836393737"));
        final String givenPhoneNumber = "375446753423";

        final Object actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
        final Object expected = new JT808RegistrationMessage(givenPhoneNumber, "86977");
        assertEquals(expected, actual);
    }
}
