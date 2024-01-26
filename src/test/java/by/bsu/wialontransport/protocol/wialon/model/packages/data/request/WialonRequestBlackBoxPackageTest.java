package by.bsu.wialontransport.protocol.wialon.model.packages.data.request;

import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class WialonRequestBlackBoxPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonRequestBlackBoxPackage givenPackage = new WialonRequestBlackBoxPackage(emptyList());

        final String actual = givenPackage.findPrefix();
        final String expected = "#B#";
        assertEquals(expected, actual);
    }
}
