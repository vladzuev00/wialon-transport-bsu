package by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage.PREFIX;
import static by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertSame;

public final class WialonResponseSingleLocationPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final var givenPackage = new WialonResponseSingleLocationPackage(PACKAGE_FIX_SUCCESS);

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
