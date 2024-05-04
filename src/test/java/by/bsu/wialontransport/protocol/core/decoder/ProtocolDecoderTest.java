package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder.NoPackageDecoderException;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ProtocolDecoderTest {
    private static final Object GIVEN_PREFIX = new Object();
    private static final Object GIVEN_SOURCE = new Object();

    @Mock
    private PackageDecoder<Object, Object> firstMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Object> secondMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Object> thirdMockedPackageDecoder;

    private ProtocolDecoder<Object, Object> protocolDecoder;

    @Before
    public void initializeProtocolDecoder() {
        protocolDecoder = new TestProtocolDecoder(
                List.of(
                        firstMockedPackageDecoder,
                        secondMockedPackageDecoder,
                        thirdMockedPackageDecoder
                ),
                GIVEN_PREFIX,
                GIVEN_SOURCE
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bufferShouldBeDecoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final List<Object> givenOut = mock(List.class);

        when(firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(true);

        final Package givenRequest = mock(Package.class);
        when(secondMockedPackageDecoder.decode(same(GIVEN_SOURCE))).thenReturn(givenRequest);

        protocolDecoder.decode(givenContext, givenBuffer, givenOut);

        verifyNoInteractions(thirdMockedPackageDecoder);
        verify(givenBuffer, times(1)).retain();
        verify(givenOut, times(1)).add(same(givenRequest));
        verify(givenBuffer, times(1)).release();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NoPackageDecoderException.class)
    public void bufferShouldNotBeDecodedBecauseOfNoSuitablePackageDecoder() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final List<Object> givenOut = mock(List.class);

        when(firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(thirdMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);

        protocolDecoder.decode(givenContext, givenBuffer, givenOut);
    }

    private static final class TestProtocolDecoder extends ProtocolDecoder<Object, Object> {
        private final Object source;
        private final Object prefix;

        public TestProtocolDecoder(final List<PackageDecoder<Object, Object>> packageDecoders,
                                   final Object prefix,
                                   final Object source) {
            super(packageDecoders);
            this.prefix = prefix;
            this.source = source;
        }

        @Override
        protected Object createSource(final ByteBuf buffer) {
            return source;
        }

        @Override
        protected Object getPrefix(final Object source) {
            return prefix;
        }
    }
}
