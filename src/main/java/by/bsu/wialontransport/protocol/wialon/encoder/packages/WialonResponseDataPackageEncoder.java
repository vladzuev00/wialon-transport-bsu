package by.bsu.wialontransport.protocol.wialon.encoder.packages;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseDataPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseDataPackageEncoder extends WialonPackageEncoder<WialonResponseDataPackage> {

    public WialonResponseDataPackageEncoder() {
        super(WialonResponseDataPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseDataPackage response) {
        return Byte.toString(response.getStatus().getValue());
    }
}
