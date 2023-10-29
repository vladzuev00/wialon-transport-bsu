package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage.PREFIX;

@Component
public final class RequestPingPackageDeserializer extends AbstractPackageDeserializer {

    public RequestPingPackageDeserializer() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestPingPackage deserializeByMessage(final String message) {
        return new WialonRequestPingPackage();
    }
}
