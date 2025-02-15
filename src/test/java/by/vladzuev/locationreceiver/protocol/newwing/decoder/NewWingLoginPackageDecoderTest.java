package by.vladzuev.locationreceiver.protocol.newwing.decoder;

import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLoginPackage;
import by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class NewWingLoginPackageDecoderTest {
    private final NewWingLoginPackageDecoder decoder = new NewWingLoginPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        try (final MockedStatic<NewWingUtil> mockedUtil = mockStatic(NewWingUtil.class)) {
            final ByteBuf givenBuffer = mock(ByteBuf.class);

            final String givenImei = "3009";
            mockedUtil.when(() -> NewWingUtil.decodeImei(givenBuffer)).thenReturn(givenImei);

            final NewWingLoginPackage actual = decoder.decodeInternal(givenBuffer);
            final NewWingLoginPackage expected = new NewWingLoginPackage(givenImei);
            assertEquals(expected, actual);
        }
    }
}
