package by.bsu.wialontransport.protocol.wialon.decoder;

import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.util.ReflectionUtil.findStaticFieldValue;
import static org.junit.Assert.assertTrue;

public final class WialonProtocolDecoderTest {
    private static final String FIELD_NAME_PACKAGE_PREFIX_REGEX = "PACKAGE_PREFIX_REGEX";

    private final String packagePrefixRegex = findStaticFieldValue(
            WialonProtocolDecoder.class,
            FIELD_NAME_PACKAGE_PREFIX_REGEX,
            String.class
    );

    @Test
    public void packagePrefixesShouldMatchRegex() {
        final List<String> givenPrefixes = List.of("#L#", "#P#", "#D#", "#B#");

        final boolean actual = givenPrefixes.stream().allMatch(prefix -> prefix.matches(this.packagePrefixRegex));
        assertTrue(actual);
    }

    @Test
    public void packagePrefixesShouldNotMatchRegex() {
        final List<String> givenPrefixes = List.of("##", "P#", "#D", "");

        final boolean actual = givenPrefixes.stream().noneMatch(prefix -> prefix.matches(this.packagePrefixRegex));
        assertTrue(actual);
    }
}