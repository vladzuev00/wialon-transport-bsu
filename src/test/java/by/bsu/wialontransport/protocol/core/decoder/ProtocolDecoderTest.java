package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.protocolpackage.Package;
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
    private static final Object GIVEN_SOURCE = new Object();

    @Mock
    private PackageDecoder<Object, Package> firstMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Package> secondMockedPackageDecoder;

    @Mock
    private PackageDecoder<Object, Package> thirdMockedPackageDecoder;

    private ProtocolDecoder<?, ?, ?> protocolDecoder;

    @Before
    public void initializeProtocolDecoder() {
        this.protocolDecoder = new TestProtocolDecoder(
                List.of(
                        this.firstMockedPackageDecoder,
                        this.secondMockedPackageDecoder,
                        this.thirdMockedPackageDecoder
                ),
                GIVEN_SOURCE
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bufferShouldBeDecoded() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenByteBuf = mock(ByteBuf.class);
        final List<Object> givenOutObjects = mock(List.class);

        when(this.firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_SOURCE))).thenReturn(false);
        when(this.secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_SOURCE))).thenReturn(true);

        final Package givenPackage = mock(Package.class);
        when(this.secondMockedPackageDecoder.decode(same(GIVEN_SOURCE))).thenReturn(givenPackage);

        this.protocolDecoder.decode(givenContext, givenByteBuf, givenOutObjects);

        verifyNoInteractions(this.thirdMockedPackageDecoder);
        verify(givenOutObjects, times(1)).add(same(givenPackage));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void bufferShouldNotBeDecodedBecauseOfNoSuitablePackageDecoder() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final ByteBuf givenByteBuf = mock(ByteBuf.class);
        final List<Object> givenOutObjects = mock(List.class);

        when(this.firstMockedPackageDecoder.isAbleToDecode(same(GIVEN_SOURCE))).thenReturn(false);
        when(this.secondMockedPackageDecoder.isAbleToDecode(same(GIVEN_SOURCE))).thenReturn(false);
        when(this.thirdMockedPackageDecoder.isAbleToDecode(same(GIVEN_SOURCE))).thenReturn(false);

        this.protocolDecoder.decode(givenContext, givenByteBuf, givenOutObjects);
    }

    private static final class TestProtocolDecoder
            extends ProtocolDecoder<Object, Package, PackageDecoder<Object, Package>> {
        private final Object source;

        public TestProtocolDecoder(final List<PackageDecoder<Object, Package>> packageDecoders, final Object source) {
            super(packageDecoders);
            this.source = source;
        }

        @Override
        protected Object createSource(final ByteBuf buffer) {
            return this.source;
        }
    }
}
