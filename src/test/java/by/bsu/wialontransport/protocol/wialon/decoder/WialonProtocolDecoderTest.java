package by.bsu.wialontransport.protocol.wialon.decoder;

import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.decoder.WialonProtocolDecoder.PREFIX_REGEX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class WialonProtocolDecoderTest {

    @Test
    public void packagePrefixesShouldMatchRegex() {
        final List<String> givenPrefixes = List.of("#L#", "#P#", "#D#", "#B#");

        final boolean actual = givenPrefixes.stream().allMatch(prefix -> prefix.matches(PREFIX_REGEX));
        assertTrue(actual);
    }

    @Test
    public void packagePrefixesShouldNotMatchRegex() {
        final List<String> givenPrefixes = List.of("##", "P#", "#D", "");

        final boolean actual = givenPrefixes.stream().anyMatch(prefix -> prefix.matches(PREFIX_REGEX));
        assertFalse(actual);
    }
}
