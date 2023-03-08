package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage.PREFIX;

@Component
public final class RequestPingPackageDeserializer extends AbstractPackageDeserializer {

    public RequestPingPackageDeserializer() {
        super(PREFIX);
    }

    @Override
    protected RequestPingPackage deserializeByMessage(final String message) {
        return new RequestPingPackage();
    }
}
