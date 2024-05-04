package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolStringDecoder.PackagePrefixExtractingException;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ProtocolStringDecoderTest {
    private static final String GIVEN_PACKAGE_PREFIX_REGEX = "^#.+#";
    private static final Charset EXPECTED_SOURCE_CHARSET = UTF_8;

    private final ProtocolStringDecoder decoder = new ProtocolStringDecoder(null, GIVEN_PACKAGE_PREFIX_REGEX) {
    };

    @Test
    public void sourceShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenReadableBytes = 6;
        when(givenBuffer.readableBytes()).thenReturn(givenReadableBytes);

        final String givenSource = "source";
        when(givenBuffer.readCharSequence(eq(givenReadableBytes), same(EXPECTED_SOURCE_CHARSET)))
                .thenReturn(givenSource);

        final String actual = decoder.createSource(givenBuffer);
        assertSame(givenSource, actual);
    }

    @Test
    public void packagePrefixShouldBeExtracted() {
        final String givenSource = "#L#11112222333344445555;111";

        final String actual = decoder.getPrefix(givenSource);
        final String expected = "#L#";
        assertEquals(expected, actual);
    }

    @Test(expected = PackagePrefixExtractingException.class)
    public void packagePrefixShouldNotBeExtracted() {
        final String givenSource = "#L11112222333344445555;111";

        decoder.getPrefix(givenSource);
    }
}
