package by.bsu.wialontransport.protocol.core.decoder.packages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class PrefixedByStringTextPackageDecoderTest {
    private final TestPrefixedByStringTextPackageDecoder decoder = new TestPrefixedByStringTextPackageDecoder();

    @Test
    public void prefixShouldBeRead() {
        final String givenSource = "#PREFIX#content";
        final int givenLength = 8;

        final String actual = decoder.readPrefix(givenSource, givenLength);
        final String expected = "#PREFIX#";
        assertEquals(expected, actual);
    }

    @Test
    public void prefixesShouldBeEqual() {
        final String givenFirst = "#PREFIX#";
        final String givenSecond = "#PREFIX#";

        assertTrue(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixesShouldNotBeEqual() {
        final String givenFirst = "#PREFIX#";
        final String givenSecond = "#PREFI#";

        assertFalse(decoder.isEqual(givenFirst, givenSecond));
    }

    @Test
    public void prefixLengthShouldBeGot() {
        final String givenRequiredPrefix = "#PREFIX#";

        final int actual = decoder.getLength(givenRequiredPrefix);
        final int expected = 8;
        assertEquals(expected, actual);
    }

    @Test
    public void sourceShouldBeSkipped() {
        final String givenSource = "#PREFIX#content";
        final int givenLength = 8;

        final String actual = decoder.skip(givenSource, givenLength);
        final String expected = "content";
        assertEquals(expected, actual);
    }

    private static final class TestPrefixedByStringTextPackageDecoder extends PrefixedByStringTextPackageDecoder {

        public TestPrefixedByStringTextPackageDecoder() {
            super(null);
        }

        @Override
        protected Object decodeInternal(final String source) {
            throw new UnsupportedOperationException();
        }
    }
}
