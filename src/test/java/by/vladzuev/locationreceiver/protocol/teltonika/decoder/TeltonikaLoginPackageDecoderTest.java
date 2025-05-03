package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.holder.TeltonikaLoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class TeltonikaLoginPackageDecoderTest {

    @Mock
    private TeltonikaLoginSuccessHolder mockedLoginSuccessHolder;

    private TeltonikaLoginPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new TeltonikaLoginPackageDecoder(mockedLoginSuccessHolder);
    }

    @Test
    public void decoderShouldBeAbleToDecode() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        when(mockedLoginSuccessHolder.isSuccess()).thenReturn(false);

        assertTrue(decoder.isAbleDecode(givenBuffer));

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void decoderShouldNotBeAbleToDecode() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        when(mockedLoginSuccessHolder.isSuccess()).thenReturn(true);

        assertFalse(decoder.isAbleDecode(givenBuffer));

        verifyNoInteractions(givenBuffer);
    }

    @Test
    public void packageShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("000F313233343536373839303132333435"));

        final TeltonikaRequestLoginPackage actual = decoder.decodeInternal(givenBuffer);
        final TeltonikaRequestLoginPackage expected = new TeltonikaRequestLoginPackage("123456789012345");
        assertEquals(expected, actual);

        verifyNoInteractions(mockedLoginSuccessHolder);
    }
}
