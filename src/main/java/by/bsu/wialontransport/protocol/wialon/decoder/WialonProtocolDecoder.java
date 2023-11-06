package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolStringDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;

import java.util.List;

public final class WialonProtocolDecoder extends ProtocolStringDecoder<WialonPackageDecoder<?>> {
    private static final String PACKAGE_PREFIX_REGEX = "^#.+#";

    public WialonProtocolDecoder(final List<WialonPackageDecoder<?>> packageDecoders) {
        super(packageDecoders, PACKAGE_PREFIX_REGEX);
    }
}
