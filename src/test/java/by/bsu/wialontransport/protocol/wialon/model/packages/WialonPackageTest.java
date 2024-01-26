package by.bsu.wialontransport.protocol.wialon.model.packages;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonPackageTest {

    @Test
    public void postfixShouldBeFound() {
        final WialonPackage givenPackage = createPackage();

        final String actual = givenPackage.findPostfix();
        final String expected = "\r\n";
        assertEquals(expected, actual);
    }

    private static WialonPackage createPackage() {
        return new WialonPackage() {

            @Override
            public String findPrefix() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
