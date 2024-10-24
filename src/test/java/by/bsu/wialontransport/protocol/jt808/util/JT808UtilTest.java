package by.bsu.wialontransport.protocol.jt808.util;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.protocol.jt808.util.JT808Util.*;
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

    @Test
    public void manufacturerIdShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("3836393737"));

        final String actual = decodeManufacturerId(givenBuffer);
        final String expected = "86977";
        assertEquals(expected, actual);
    }

    @Test
    public void latitudeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("015881C9"));

        final double actual = decodeLatitude(givenBuffer);
        final double expected = 22.577609;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void longitudeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("06CA8E05"));

        final double actual = decodeLongitude(givenBuffer);
        final double expected = 11.3937925;
        assertEquals(expected, actual);
    }

    @Test
    public void dateTimeShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("230727070914"));

        final LocalDateTime actual = decodeDateTime(givenBuffer);
        final LocalDateTime expected = LocalDateTime.of(2023, 7, 27, 7, 9, 14);
        assertEquals(expected, actual);
    }
}
