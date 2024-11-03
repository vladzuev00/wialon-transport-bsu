package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseDataPackageEncoder extends WialonPackageEncoder<WialonResponseSingleLocationPackage> {

    public WialonResponseDataPackageEncoder() {
        super(WialonResponseSingleLocationPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseSingleLocationPackage response) {
        return Byte.toString(response.getStatus().getValue());
    }
}
