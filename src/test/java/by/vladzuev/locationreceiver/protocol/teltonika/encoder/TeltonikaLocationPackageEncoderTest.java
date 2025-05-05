package by.vladzuev.locationreceiver.protocol.teltonika.encoder;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaResponseLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class TeltonikaLocationPackageEncoderTest {
    private final TeltonikaLocationPackageEncoder encoder = new TeltonikaLocationPackageEncoder();

    @Test
    public void responseShouldBeEncoded() {
        final TeltonikaResponseLocationPackage givenResponse = new TeltonikaResponseLocationPackage(10);

        final byte[] actual = encoder.encodeInternal(givenResponse);
        final byte[] expected = {10};
        assertArrayEquals(expected, actual);
    }
}
