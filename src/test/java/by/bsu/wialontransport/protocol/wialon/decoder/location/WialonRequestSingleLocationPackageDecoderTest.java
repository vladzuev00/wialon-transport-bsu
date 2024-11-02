package by.bsu.wialontransport.protocol.wialon.decoder.location;

import by.bsu.wialontransport.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestSingleLocationPackage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class WialonRequestSingleLocationPackageDecoderTest {
    private final WialonRequestSingleLocationPackageDecoder decoder = new WialonRequestSingleLocationPackageDecoder(null);

    @Test
    public void messageShouldBeDecodedInternally() {
        final String givenMessage = "test-message";
        final WialonLocationParser givenLocationParser = mock(WialonLocationParser.class);

        final WialonLocation givenLocation = mock(WialonLocation.class);
        when(givenLocationParser.parse(same(givenMessage))).thenReturn(givenLocation);

        final var actual = decoder.decodeMessageInternal(givenMessage, givenLocationParser);
        final var expected = new WialonRequestSingleLocationPackage(givenLocation);
        assertEquals(expected, actual);
    }
}
