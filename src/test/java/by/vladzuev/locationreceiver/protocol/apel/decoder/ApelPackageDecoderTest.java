package by.vladzuev.locationreceiver.protocol.apel.decoder;

import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public final class ApelPackageDecoderTest {
    private final TestDecoder decoder = new TestDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenId = 255;
        when(givenBuffer.readIntLE()).thenReturn(givenId);

        final Object actual = decoder.decodeInternal(givenBuffer);
        final TestPackage expected = new TestPackage(givenId);
        assertEquals(expected, actual);

        verify(givenBuffer, times(1)).skipBytes(eq(Short.BYTES));
    }

    @Value
    private static class TestPackage {
        int id;
    }

    private static final class TestDecoder extends ApelPackageDecoder {

        public TestDecoder() {
            super(Short.MAX_VALUE);
        }

        @Override
        protected TestPackage decodeStartingFromBody(final ByteBuf buffer) {
            final int id = buffer.readIntLE();
            return new TestPackage(id);
        }
    }
}
