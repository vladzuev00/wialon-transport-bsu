package by.vladzuev.locationreceiver.protocol.wialon.model.location.request;

import org.junit.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestBulkLocationPackage.PREFIX;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;

public final class WialonRequestBulkLocationPackageTest {

    @Test
    public void prefixShouldBeGot() {
        final WialonRequestBulkLocationPackage givenPackage = new WialonRequestBulkLocationPackage(emptyList());

        final String actual = givenPackage.getPrefix();
        assertSame(PREFIX, actual);
    }
}
