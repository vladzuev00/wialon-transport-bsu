package by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonResponseBulkLocationPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonResponseBulkLocationPackage givenPackage = new WialonResponseBulkLocationPackage(5);

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
