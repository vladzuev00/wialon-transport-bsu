package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaRequestLocationPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class TeltonikaLocationPackageDecoderTest {

    @Mock
    private TeltonikaLocationDecoder mockedLocationDecoder;

    private TeltonikaLocationPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new TeltonikaLocationPackageDecoder(null, mockedLocationDecoder);
    }

    @Test
    public void packageShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final byte givenLocationCount = 2;
        when(givenBuffer.readByte()).thenReturn(givenLocationCount);

        final TeltonikaLocation firstGivenLocation = new TeltonikaLocation(
                now(),
                5.0,
                6.0,
                (short) 7,
                (short) 8,
                (byte) 9,
                (short) 10
        );
        final TeltonikaLocation secondGivenLocation = new TeltonikaLocation(
                now(),
                6.0,
                7.0,
                (short) 8,
                (short) 9,
                (byte) 10,
                (short) 11
        );
        when(mockedLocationDecoder.decode(same(givenBuffer)))
                .thenReturn(firstGivenLocation)
                .thenReturn(secondGivenLocation);

        final TeltonikaRequestLocationPackage actual = decoder.decodeInternal(givenBuffer);
        final var expected = new TeltonikaRequestLocationPackage(List.of(firstGivenLocation, secondGivenLocation));
        assertEquals(expected, actual);
    }
}
