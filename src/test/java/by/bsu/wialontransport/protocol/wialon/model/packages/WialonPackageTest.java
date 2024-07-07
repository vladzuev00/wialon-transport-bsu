package by.bsu.wialontransport.protocol.wialon.model.packages;

import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;
import static org.junit.Assert.assertSame;

public final class WialonPackageTest {

    @Test
    public void postfixShouldBeFound() {
        final WialonPackage givenPackage = createPackage();

        final String actual = givenPackage.getPostfix();
        assertSame(POSTFIX, actual);
    }

    private static WialonPackage createPackage() {
        return new WialonPackage() {

            @Override
            public String getPrefix() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
