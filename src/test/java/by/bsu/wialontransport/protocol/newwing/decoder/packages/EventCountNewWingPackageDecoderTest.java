package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.builder.EventCountNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.mockito.Mockito.*;

public final class EventCountNewWingPackageDecoderTest {
    private final EventCountNewWingPackageDecoder decoder = new EventCountNewWingPackageDecoder();

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final EventCountNewWingPackageBuilder givenPackageBuilder = mock(EventCountNewWingPackageBuilder.class);

        final short givenEventCount = 20;
        final short givenFrameEventCount = 5;
        when(givenBuffer.readShortLE())
                .thenReturn(givenEventCount)
                .thenReturn(givenFrameEventCount);

        this.decoder.decodeUntilChecksum(givenBuffer, givenPackageBuilder);

        verify(givenPackageBuilder, times(1)).setEventCount(eq(givenEventCount));
        verify(givenPackageBuilder, times(1)).setFrameEventCount(eq(givenFrameEventCount));
    }
}
