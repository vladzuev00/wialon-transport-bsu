package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PackageDecoderTest {
    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";

    private final PackageDecoder<String, Object, Package> decoder = new PackageDecoder<>(GIVEN_PACKAGE_PREFIX) {

        @Override
        public Package decode(final Object source) {
            throw new RuntimeException();
        }

    };

    @Test
    public void decoderShouldBeAbleToDecode() {
        final String givenPackagePrefix = "#TEST#";

        final boolean actual = this.decoder.isAbleToDecode(givenPackagePrefix);
        assertTrue(actual);
    }

    @Test
    public void decoderShouldNotBeAbleToDecode() {
        final String givenPackagePrefix = "#tEST#";

        final boolean actual = this.decoder.isAbleToDecode(givenPackagePrefix);
        assertFalse(actual);
    }

}
