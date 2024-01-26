package by.bsu.wialontransport.protocol.wialon.model.packages.data.request;

import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestDataPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestDataPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonRequestDataPackage givenPackage = new WialonRequestDataPackage(WialonData.builder().build());

        final String actual = givenPackage.findPrefix();
        assertSame(PREFIX, actual);
    }
}
