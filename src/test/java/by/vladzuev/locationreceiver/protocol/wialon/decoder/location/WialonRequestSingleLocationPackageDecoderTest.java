package by.vladzuev.locationreceiver.protocol.wialon.decoder.location;

import by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestSingleLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class WialonRequestSingleLocationPackageDecoderTest {
    private final WialonRequestSingleLocationPackageDecoder decoder = new WialonRequestSingleLocationPackageDecoder(null);

    @Test
    public void messageShouldBeDecodedInternally() {
        final String givenMessage = "test-message";
        final WialonLocationParser givenLocationParser = mock(WialonLocationParser.class);

        final WialonLocation givenLocation = mock(WialonLocation.class);
        when(givenLocationParser.parse(same(givenMessage))).thenReturn(givenLocation);

        final var actual = decoder.decodeMessageInternal(givenMessage, givenLocationParser);
        final WialonRequestSingleLocationPackage expected = new WialonRequestSingleLocationPackage(givenLocation);
        assertEquals(expected, actual);
    }
}
