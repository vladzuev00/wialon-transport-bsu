package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonRequestLoginPackageDecoderTest {
    private final WialonRequestLoginPackageDecoder decoder = new WialonRequestLoginPackageDecoder();

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "11111222223333344444;1111";

        final WialonRequestLoginPackage actual = decoder.decodeMessage(givenMessage);
        final WialonRequestLoginPackage expected = new WialonRequestLoginPackage("11111222223333344444", "1111");
        assertEquals(expected, actual);
    }
}
