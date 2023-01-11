package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage;
import org.springframework.stereotype.Component;

@Component
public final class RequestPingPackageDeserializer implements PackageDeserializer {

    @Override
    public RequestPingPackage deserialize(final String source) {
        return new RequestPingPackage();
    }
}
