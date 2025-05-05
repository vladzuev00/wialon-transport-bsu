package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.holder.TeltonikaLoginSuccessHolder;
import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class TeltonikaPackageDecoderTest {

    @Mock
    private TeltonikaLoginSuccessHolder mockedLoginSuccessHolder;

    private TeltonikaPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new TestTeltonikaPackageDecoder(mockedLoginSuccessHolder);
    }

    @Test
    public void decoderShouldBeAbleToDecode() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        when(mockedLoginSuccessHolder.isSuccess()).thenReturn(true);

        assertTrue(decoder.isAbleDecode(givenBuffer));

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void decoderShouldNotBeAbleToDecode() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        when(mockedLoginSuccessHolder.isSuccess()).thenReturn(false);

        assertFalse(decoder.isAbleDecode(givenBuffer));

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void packageShouldBeDecoded() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenId = 255;
        when(givenBuffer.readInt()).thenReturn(givenId);

        final Object actual = decoder.decode(givenBuffer);
        final TestPackage expected = new TestPackage(givenId);
        assertEquals(expected, actual);

        verify(givenBuffer, times(1)).skipBytes(eq(Short.BYTES));
    }

    @Value
    private static class TestPackage {
        int id;
    }

    private static final class TestTeltonikaPackageDecoder extends TeltonikaPackageDecoder {

        public TestTeltonikaPackageDecoder(final TeltonikaLoginSuccessHolder loginSuccessHolder) {
            super(loginSuccessHolder, true);
        }

        @Override
        protected TestPackage decodeInternal(final ByteBuf buffer) {
            final int id = buffer.readInt();
            return new TestPackage(id);
        }
    }
}
