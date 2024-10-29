package by.bsu.wialontransport.protocol.wialon.model.packages.location.request;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestSingleLocationPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestSingleLocationPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonRequestSingleLocationPackage givenPackage = new WialonRequestSingleLocationPackage(null);

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
