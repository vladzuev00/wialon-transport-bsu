package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocation;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocationPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class NewWingLocationPackageDecoderTest {

    @Mock
    private NewWingLocationIteratorFactory mockedLocationIteratorFactory;

    private NewWingLocationPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new NewWingLocationPackageDecoder(mockedLocationIteratorFactory);
    }

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final NewWingLocationIterator givenIterator = mock(NewWingLocationIterator.class);
        when(mockedLocationIteratorFactory.create(same(givenBuffer))).thenReturn(givenIterator);

        when(givenIterator.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        final NewWingLocation firstGivenLocation = mock(NewWingLocation.class);
        final NewWingLocation secondGivenLocation = mock(NewWingLocation.class);
        when(givenIterator.next())
                .thenReturn(firstGivenLocation)
                .thenReturn(secondGivenLocation);

        final NewWingLocationPackage actual = decoder.decodeInternal(givenBuffer);
        final var expected = new NewWingLocationPackage(List.of(firstGivenLocation, secondGivenLocation));
        assertEquals(expected, actual);
    }
}
