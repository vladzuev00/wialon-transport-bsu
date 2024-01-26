package by.bsu.wialontransport.protocol.wialon.model.packages.data.response;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonResponseBlackBoxPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonResponseBlackBoxPackage givenPackage = new WialonResponseBlackBoxPackage(5);

        final String actual = givenPackage.findPrefix();
        assertSame(PREFIX, actual);
    }
}
