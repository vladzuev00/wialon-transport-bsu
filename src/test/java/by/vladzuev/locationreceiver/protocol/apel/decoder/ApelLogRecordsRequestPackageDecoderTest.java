package by.vladzuev.locationreceiver.protocol.apel.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public final class ApelLogRecordsRequestPackageDecoderTest {
    private final ApelLogRecordsRequestPackageDecoder decoder = new ApelLogRecordsRequestPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        assertThrows(UnsupportedOperationException.class, () -> decoder.decodeStartingFromBody(givenBuffer));
    }
}
