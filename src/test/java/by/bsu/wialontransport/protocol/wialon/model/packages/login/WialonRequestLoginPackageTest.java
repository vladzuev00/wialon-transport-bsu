package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage.PREFIX;
import static org.junit.Assert.assertSame;

public final class WialonRequestLoginPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonRequestLoginPackage givenPackage = new WialonRequestLoginPackage(
                "11112222333344445555",
                "111"
        );

        final String actual = givenPackage.findPrefix();
        assertSame(PREFIX, actual);
    }
}
