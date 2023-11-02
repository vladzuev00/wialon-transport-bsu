package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class WialonRequestLoginPackageDecoderTest {
    private final WialonRequestLoginPackageDecoder decoder = new WialonRequestLoginPackageDecoder();

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "11111222223333344444;1111";

        final WialonRequestLoginPackage actual = this.decoder.decodeMessage(givenMessage);
        final WialonRequestLoginPackage expected = new WialonRequestLoginPackage(
                "11111222223333344444",
                "1111"
        );
        assertEquals(expected, actual);
    }
}
