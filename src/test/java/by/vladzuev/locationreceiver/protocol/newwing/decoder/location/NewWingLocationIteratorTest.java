package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.NewWingLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static by.vladzuev.locationreceiver.protocol.newwing.decoder.location.NewWingLocationIterator.LOCATION_BYTE_COUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class NewWingLocationIteratorTest {

    @Mock
    private NewWingLocationDecoder mockedDecoder;

    @Mock
    private ByteBuf mockedBuffer;

    private NewWingLocationIterator iterator;

    @BeforeEach
    public void initializeIterator() {
        iterator = new NewWingLocationIterator(mockedDecoder, mockedBuffer);
    }

    @Test
    public void bufferShouldHaveNextLocation() {
        when(mockedBuffer.readableBytes()).thenReturn(LOCATION_BYTE_COUNT);

        assertTrue(iterator.hasNext());
    }

    @Test
    public void bufferShouldNotHaveNextLocation() {
        final int givenReadableBytes = LOCATION_BYTE_COUNT - 1;
        when(mockedBuffer.readableBytes()).thenReturn(givenReadableBytes);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void nextLocationShouldBeGot() {
        when(mockedBuffer.readableBytes()).thenReturn(LOCATION_BYTE_COUNT);

        final NewWingLocation givenLocation = mock(NewWingLocation.class);
        when(mockedDecoder.decode(same(mockedBuffer))).thenReturn(givenLocation);

        final NewWingLocation actual = iterator.next();
        assertSame(givenLocation, actual);
    }

    @Test
    public void nextLocationShouldNotBeGot() {
        final int givenReadableBytes = LOCATION_BYTE_COUNT - 1;
        when(mockedBuffer.readableBytes()).thenReturn(givenReadableBytes);

        assertThrows(NoSuchElementException.class, () -> iterator.next());

        verifyNoInteractions(mockedDecoder);
    }
}
