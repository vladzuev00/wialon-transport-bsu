package by.bsu.wialontransport.protocol.newwing.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder.PREFIX_CHARSET;
import static by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder.PREFIX_LENGTH;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingProtocolDecoderTest {
    private final NewWingProtocolDecoder decoder = new NewWingProtocolDecoder(emptyList());

    @Test
    public void packagePrefixShouldBeGot() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final String givenPrefix = "TEST";
        when(givenBuffer.readCharSequence(eq(PREFIX_LENGTH), same(PREFIX_CHARSET))).thenReturn(givenPrefix);

        final String actual = decoder.getPrefix(givenBuffer);
        assertSame(givenPrefix, actual);
    }
}
