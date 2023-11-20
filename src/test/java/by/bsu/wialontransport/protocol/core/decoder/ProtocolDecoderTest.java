package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder.NoSuitablePackageDecoderException;
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
    private PackageDecoder<Object, Object, Package> firstMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Object, Package> secondMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Object, Package> thirdMockedPackageDecoder;

    private ProtocolDecoder<Object, Object> protocolDecoder;

    @Before
    public void initializeProtocolDecoder() {
        this.protocolDecoder = new TestProtocolDecoder(
                List.of(
                        this.firstMockedPackageDecoder,
                        this.secondMockedPackageDecoder,
                        this.thirdMockedPackageDecoder
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
        final List<Object> givenOutObjects = mock(List.class);

        when(this.firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(this.secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(true);

        final Package givenPackage = mock(Package.class);
        when(this.secondMockedPackageDecoder.decode(same(GIVEN_SOURCE))).thenReturn(givenPackage);

        this.protocolDecoder.decode(givenContext, givenBuffer, givenOutObjects);

        verifyNoInteractions(this.thirdMockedPackageDecoder);
        verify(givenOutObjects, times(1)).add(same(givenPackage));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NoSuitablePackageDecoderException.class)
    public void bufferShouldNotBeDecodedBecauseOfNoSuitablePackageDecoder() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final List<Object> givenOutObjects = mock(List.class);

        when(this.firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(this.secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);
        when(this.thirdMockedPackageDecoder.isAbleToDecode(same(GIVEN_PREFIX))).thenReturn(false);

        this.protocolDecoder.decode(givenContext, givenBuffer, givenOutObjects);
    }

    private static final class TestProtocolDecoder extends ProtocolDecoder<Object, Object> {
        private final Object source;
        private final Object prefix;

        public TestProtocolDecoder(final List<PackageDecoder<Object, Object, ?>> packageDecoders,
                                   final Object prefix,
                                   final Object source) {
            super(packageDecoders);
            this.prefix = prefix;
            this.source = source;
        }

        @Override
        protected Object createSource(final ByteBuf buffer) {
            return this.source;
        }

        @Override
        protected Object extractPackagePrefix(final Object source) {
            return this.prefix;
        }
    }
}
