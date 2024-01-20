package by.bsu.wialontransport.protocol.core.encoder;

import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder.NoSuitablePackageEncoderException;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nl.altindag.log.LogCaptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ProtocolEncoderTest {
    private static final Charset EXPECTED_ENCODED_RESPONSE_CHARSET = UTF_8;

    private final LogCaptor logCaptor = forClass(ProtocolEncoder.class);

    @Mock
    private PackageEncoder<?> firstMockedPackageEncoder;

    @Mock
    private PackageEncoder<?> secondMockedPackageEncoder;

    @Mock
    private PackageEncoder<?> thirdMockedPackageEncoder;

    private ProtocolEncoder protocolEncoder;

    @Before
    public void initializeProtocolEncoder() {
        protocolEncoder = new TestProtocolEncoder(
                List.of(
                        firstMockedPackageEncoder,
                        secondMockedPackageEncoder,
                        thirdMockedPackageEncoder
                )
        );
    }

    @Test
    public void responseShouldBeEncoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Package givenResponse = new Package() {
        };
        final ByteBuf givenOutBuffer = mock(ByteBuf.class);

        when(firstMockedPackageEncoder.isAbleToEncode(same(givenResponse))).thenReturn(false);
        when(secondMockedPackageEncoder.isAbleToEncode(same(givenResponse))).thenReturn(true);

        final String givenEncodedResponse = "response";
        when(secondMockedPackageEncoder.encode(same(givenResponse))).thenReturn(givenEncodedResponse);

        protocolEncoder.encode(givenContext, givenResponse, givenOutBuffer);

        verify(givenOutBuffer, times(1)).writeCharSequence(
                same(givenEncodedResponse),
                same(EXPECTED_ENCODED_RESPONSE_CHARSET)
        );
        verifyNoInteractions(thirdMockedPackageEncoder);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Response was encoded to 'response'");
        assertEquals(expectedLogs, actualLogs);
    }

    @Test(expected = NoSuitablePackageEncoderException.class)
    public void responseShouldNotBeEncodedBecauseOfNoSuitableEncoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Package givenResponse = new Package() {
        };
        final ByteBuf givenOutBuffer = mock(ByteBuf.class);

        when(firstMockedPackageEncoder.isAbleToEncode(same(givenResponse))).thenReturn(false);
        when(secondMockedPackageEncoder.isAbleToEncode(same(givenResponse))).thenReturn(false);
        when(thirdMockedPackageEncoder.isAbleToEncode(same(givenResponse))).thenReturn(false);

        protocolEncoder.encode(givenContext, givenResponse, givenOutBuffer);
    }

    private static final class TestProtocolEncoder extends ProtocolEncoder {

        public TestProtocolEncoder(final List<PackageEncoder<?>> packageEncoders) {
            super(packageEncoders);
        }

    }
}
