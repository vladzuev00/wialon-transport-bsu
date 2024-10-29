package by.bsu.wialontransport.protocol.wialon.model.packages.location.response;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseDataPackage.PREFIX;
import static by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertSame;

public final class WialonResponseDataPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonResponseDataPackage givenPackage = new WialonResponseDataPackage(PACKAGE_FIX_SUCCESS);

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
