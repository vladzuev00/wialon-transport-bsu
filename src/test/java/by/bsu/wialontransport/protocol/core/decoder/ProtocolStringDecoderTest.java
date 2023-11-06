package by.bsu.wialontransport.protocol.core.decoder;

import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ProtocolStringDecoderTest {
    private static final Charset EXPECTED_CHARSET = UTF_8;

    private final ProtocolStringDecoder<?> decoder = new ProtocolStringDecoder<>(null) {

        @Override
        protected String extractPackagePrefix(final String source) {
            throw new UnsupportedOperationException();
        }

    };

    @Test
    public void sourceShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final String givenSource = "source";
        when(givenBuffer.toString(same(EXPECTED_CHARSET))).thenReturn(givenSource);

        final String actual = this.decoder.createSource(givenBuffer);
        assertSame(givenSource, actual);
    }
}
