package by.bsu.wialontransport.protocol.wialon.decoder.location;

import by.bsu.wialontransport.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class WialonRequestLocationPackageDecoderTest {

    @Mock
    private WialonLocationParser mockedLocationParser;

    private TestWialonRequestLocationPackageDecoder decoder;

    @BeforeEach
    public void initializeDecoder() {
        decoder = new TestWialonRequestLocationPackageDecoder(mockedLocationParser);
    }

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "test-message";

        final WialonLocation givenLocation = mock(WialonLocation.class);
        when(mockedLocationParser.parse(same(givenMessage))).thenReturn(givenLocation);

        final WialonPackage actual = decoder.decodeMessage(givenMessage);
        final WialonPackage expected = new TestWialonPackage(givenLocation);
        assertEquals(expected, actual);
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static class TestWialonPackage extends WialonPackage {
        private final WialonLocation location;

        @Override
        public String getPrefix() {
            throw new UnsupportedOperationException();
        }
    }

    private static final class TestWialonRequestLocationPackageDecoder extends WialonRequestLocationPackageDecoder {
        private static final String REQUIRED_PREFIX = "#PREFIX#";

        public TestWialonRequestLocationPackageDecoder(final WialonLocationParser locationParser) {
            super(REQUIRED_PREFIX, locationParser);
        }

        @Override
        protected WialonPackage decodeMessageInternal(final String message, final WialonLocationParser locationParser) {
            final WialonLocation location = locationParser.parse(message);
            return new TestWialonPackage(location);
        }
    }
}