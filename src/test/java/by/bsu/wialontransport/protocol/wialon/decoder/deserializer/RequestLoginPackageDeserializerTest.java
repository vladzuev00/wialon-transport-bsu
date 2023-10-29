package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RequestLoginPackageDeserializerTest {

    private final RequestLoginPackageDeserializer deserializer;

    public RequestLoginPackageDeserializerTest() {
        this.deserializer = new RequestLoginPackageDeserializer();
    }

    @Test
    public void requestLoginPackageShouldBeDeserialized() {
        final String givenSource = "#L#imei;password";

        final WialonPackage actual = this.deserializer.deserialize(givenSource);
        final WialonRequestLoginPackage expected = new WialonRequestLoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
