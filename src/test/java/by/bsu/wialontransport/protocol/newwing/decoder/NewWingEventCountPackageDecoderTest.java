package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class NewWingEventCountPackageDecoderTest {
    private final NewWingEventCountPackageDecoder decoder = new NewWingEventCountPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final NewWingEventCountPackage actual = decoder.decodeInternal(givenBuffer);
        assertNotNull(actual);

        verifyNoInteractions(givenBuffer);
    }
}
