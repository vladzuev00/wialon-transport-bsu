package by.vladzuev.locationreceiver.protocol.wialon.decoder.location;

import by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.request.WialonRequestBulkLocationPackage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class WialonRequestBulkLocationPackageDecoderTest {
    private final WialonRequestBulkLocationPackageDecoder decoder = new WialonRequestBulkLocationPackageDecoder(null);

    @Test
    public void messageShouldBeDecodedInternally() {
        final String givenMessage = "test-message-1|test-message-2|test-message-3";
        final WialonLocationParser givenLocationParser = mock(WialonLocationParser.class);

        final WialonLocation firstGivenLocation = mockLocationParsing(givenLocationParser, "test-message-1");
        final WialonLocation secondGivenLocation = mockLocationParsing(givenLocationParser, "test-message-2");
        final WialonLocation thirdGivenLocation = mockLocationParsing(givenLocationParser, "test-message-3");

        final var actual = decoder.decodeMessageInternal(givenMessage, givenLocationParser);
        final WialonRequestBulkLocationPackage expected = new WialonRequestBulkLocationPackage(
                List.of(
                        firstGivenLocation,
                        secondGivenLocation,
                        thirdGivenLocation
                )
        );
        assertEquals(expected, actual);
    }

    private WialonLocation mockLocationParsing(final WialonLocationParser parser, final String source) {
        final WialonLocation location = mock(WialonLocation.class);
        when(parser.parse(eq(source))).thenReturn(location);
        return location;
    }
}
