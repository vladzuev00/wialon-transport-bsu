package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.wialon.encoder.packages.WialonPackageEncoder;

import java.util.List;

public final class WialonProtocolEncoder extends ProtocolEncoder {

    public WialonProtocolEncoder(final List<WialonPackageEncoder<?>> packageEncoders) {
        super(packageEncoders);
    }
}
