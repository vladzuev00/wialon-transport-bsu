package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponseLoginPackageEncoder extends WialonPackageEncoder<WialonResponseLoginPackage> {


    public WialonResponseLoginPackageEncoder() {
        super(WialonResponseLoginPackage.class);
    }

    @Override
    protected String encodeMessage(final WialonResponseLoginPackage response) {
        return response.getStatus().getValue();
    }
}