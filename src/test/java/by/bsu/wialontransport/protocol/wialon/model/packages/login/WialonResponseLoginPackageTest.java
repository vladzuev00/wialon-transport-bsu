package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.PREFIX;
import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION;
import static org.junit.Assert.assertSame;

public final class WialonResponseLoginPackageTest {

    @Test
    public void prefixShouldBeFound() {
        final WialonResponseLoginPackage givenPackage = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);

        final String actual = givenPackage.findPrefix();
        assertSame(PREFIX, actual);
    }
}
