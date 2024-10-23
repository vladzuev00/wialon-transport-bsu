package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.JT808Util;
import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static by.bsu.wialontransport.protocol.jt808.JT808Util.decodePhoneNumber;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class JT808PackageDecoderTest {
    private final TestJT808PackageDecoder decoder = new TestJT808PackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        try (final MockedStatic<JT808Util> mockedUtil = mockStatic(JT808Util.class)) {
            final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("003604FA0037"));

            final String givenPhoneNumber = "375448447023";
            mockedUtil.when(() -> decodePhoneNumber(same(givenBuffer))).thenReturn(givenPhoneNumber);

            final Object actual = decoder.decodeInternal(givenBuffer);
            final TestPackage expected = new TestPackage(givenPhoneNumber, (short) 55);
            assertEquals(expected, actual);
        }
    }

    @Value
    private static class TestPackage {
        String phoneNumber;
        short value;
    }

    private static final class TestJT808PackageDecoder extends JT808PackageDecoder {

        public TestJT808PackageDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final ByteBuf buffer, final String phoneNumber) {
            final short value = buffer.readShort();
            return new TestPackage(phoneNumber, value);
        }
    }
}
