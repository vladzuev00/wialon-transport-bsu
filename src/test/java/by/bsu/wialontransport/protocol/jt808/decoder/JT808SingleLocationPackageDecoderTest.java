package by.bsu.wialontransport.protocol.jt808.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static by.bsu.wialontransport.protocol.jt808.decoder.JT808SingleLocationPackageDecoder.LOCATION_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class JT808SingleLocationPackageDecoderTest {
    private final JT808SingleLocationPackageDecoder decoder = new JT808SingleLocationPackageDecoder();

    @Test
    public void locationCountShouldBeDecoded() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int actual = decoder.decodeLocationCount(givenBuffer);
        assertEquals(LOCATION_COUNT, actual);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void bufferShouldBeSkippedUntilFirstLocation() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        decoder.skipUntilFirstLocation(givenBuffer);

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void locationPrefixShouldBeSkipped() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        decoder.skipLocationPrefix(givenBuffer);

        verifyNoInteractions(givenBuffer);
    }
}