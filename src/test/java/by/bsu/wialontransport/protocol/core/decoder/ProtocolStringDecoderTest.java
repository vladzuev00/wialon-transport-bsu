package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolStringDecoder.PrefixExtractingException;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.core.decoder.ProtocolStringDecoder.SOURCE_CHARSET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ProtocolStringDecoderTest {
    private static final String GIVEN_PREFIX_REGEX = "^#.+#";

    private final ProtocolStringDecoder decoder = new ProtocolStringDecoder(null, GIVEN_PREFIX_REGEX) {
    };

    @Test
    public void sourceShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenReadableBytes = 6;
        when(givenBuffer.readableBytes()).thenReturn(givenReadableBytes);

        final String givenSource = "source";
        when(givenBuffer.readCharSequence(eq(givenReadableBytes), same(SOURCE_CHARSET))).thenReturn(givenSource);

        final String actual = decoder.createSource(givenBuffer);
        assertSame(givenSource, actual);
    }

    @Test
    public void prefixShouldBeGot() {
        final String givenSource = "#L#11112222333344445555;111";

        final String actual = decoder.getPrefix(givenSource);
        final String expected = "#L#";
        assertEquals(expected, actual);
    }

    @Test(expected = PrefixExtractingException.class)
    public void prefixShouldNotBeGot() {
        final String givenSource = "#L11112222333344445555;111";

        decoder.getPrefix(givenSource);
    }
}
