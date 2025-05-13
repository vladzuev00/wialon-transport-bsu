package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.login.ApelLoginRequestPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public final class ApelLoginRequestPackageDecoderTest {
    private final ApelLoginRequestPackageDecoder decoder = new ApelLoginRequestPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final ApelLoginRequestPackage actual = decoder.decodeStartingFromBody(givenBuffer);
        final ApelLoginRequestPackage expected = new ApelLoginRequestPackage();
        assertEquals(expected, actual);
    }
}
