package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonPackageEncoderTest {
    private final TestWialonPackageEncoder encoder = new TestWialonPackageEncoder();

    @Test
    public void responseStringShouldBeGot() {
        final TestPackage givenResponse = new TestPackage("message");

        final String actual = encoder.getString(givenResponse);
        final String expected = "#PREFIX#message\r\n";
        assertEquals(expected, actual);
    }

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class TestPackage implements WialonPackage {
        private static final String PREFIX = "#PREFIX#";

        private final String value;

        @Override
        public String getPrefix() {
            return PREFIX;
        }
    }

    private static final class TestWialonPackageEncoder extends WialonPackageEncoder<TestPackage> {

        public TestWialonPackageEncoder() {
            super(TestPackage.class);
        }

        @Override
        protected String encodeMessage(final TestPackage response) {
            return response.getValue();
        }
    }
}
