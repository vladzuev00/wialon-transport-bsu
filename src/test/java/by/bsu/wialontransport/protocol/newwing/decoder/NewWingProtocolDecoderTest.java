package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.base.AbstractContextTest;
import io.netty.buffer.ByteBuf;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingProtocolDecoderTest extends AbstractContextTest {
    private static final int EXPECTED_PACKAGE_PREFIX_LENGTH = 6;
    private static final Charset EXPECTED_PACKAGE_PREFIX_CHARSET = UTF_8;

    @Autowired
    private NewWingProtocolDecoder decoder;

    @Test
    public void packagePrefixShouldBeExtracted() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final String givenPackagePrefix = "TEST";
        when(givenBuffer.readCharSequence(eq(EXPECTED_PACKAGE_PREFIX_LENGTH), same(EXPECTED_PACKAGE_PREFIX_CHARSET)))
                .thenReturn(givenPackagePrefix);

        final String actual = decoder.extractPackagePrefix(givenBuffer);
        assertSame(givenPackagePrefix, actual);
    }
}
