package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class RequestPingPackageDeserializerTest {
    private final RequestPingPackageDeserializer deserializer;

    public RequestPingPackageDeserializerTest() {
        this.deserializer = new RequestPingPackageDeserializer();
    }

    @Test
    public void requestPingPackageShouldBeDeserialized() {
        final String givenSource = "#P#";

        final WialonPackage actual = this.deserializer.deserialize(givenSource);
        assertNotNull(actual);
    }
}
