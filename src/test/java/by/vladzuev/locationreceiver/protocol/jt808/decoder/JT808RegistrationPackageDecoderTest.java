package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import by.vladzuev.locationreceiver.protocol.jt808.model.JT808RegistrationPackage;
import by.vladzuev.locationreceiver.protocol.jt808.util.JT808Util;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static by.vladzuev.locationreceiver.protocol.jt808.util.JT808Util.decodeManufacturerId;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class JT808RegistrationPackageDecoderTest {
    private final JT808RegistrationPackageDecoder decoder = new JT808RegistrationPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        try (final MockedStatic<JT808Util> mockedUtil = mockStatic(JT808Util.class)) {
            final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("38363937"));
            final String givenPhoneNumber = "375446753423";

            final String givenManufacturerId = "86977";
            mockedUtil.when(() -> decodeManufacturerId(same(givenBuffer))).thenReturn(givenManufacturerId);

            final JT808RegistrationPackage actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
            final JT808RegistrationPackage expected = new JT808RegistrationPackage("869775446753423");
            assertEquals(expected, actual);

            assertEquals(0, givenBuffer.readableBytes());
        }
    }
}
