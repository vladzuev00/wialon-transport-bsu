package by.bsu.wialontransport.protocol.jt808.util;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static by.bsu.wialontransport.protocol.jt808.util.JT808Util.decodePhoneNumber;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JT808UtilTest {

    @Test
    public void phoneNumberShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("070061952865"));

        final String actual = decodePhoneNumber(givenBuffer);
        final String expected = "070061952865";
        assertEquals(expected, actual);
    }
}
