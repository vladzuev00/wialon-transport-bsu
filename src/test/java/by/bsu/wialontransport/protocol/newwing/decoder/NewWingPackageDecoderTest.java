package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingPackageDecoderTest {
    private final TestNewWingPackageDecoder decoder = new TestNewWingPackageDecoder();

    @Test
    public void bufferShouldBeDecodedAfterSkipPrefix() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenChecksum = 12345;
        when(givenBuffer.readIntLE()).thenReturn(givenChecksum);

        final NewWingRequestPackage actual = decoder.decodeAfterSkipPrefix(givenBuffer);
        final NewWingRequestPackage expected = new TestPackage(givenChecksum);
        assertEquals(expected, actual);
    }

    private static final class TestPackage extends NewWingRequestPackage {

        public TestPackage(final int checksum) {
            super(checksum);
        }
    }

    private static final class TestNewWingPackageDecoder extends NewWingPackageDecoder {
        private static final String PREFIX = "PREFIX";

        public TestNewWingPackageDecoder() {
            super(PREFIX);
        }

        @Override
        protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
            return TestPackage::new;
        }
    }
}