package by.vladzuev.locationreceiver.protocol.wialon.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedTextPackageDecoder;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;

public abstract class WialonPackageDecoder extends PrefixedTextPackageDecoder {

    public WialonPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final WialonPackage decodeInternal(final String source) {
        final String message = removePostfix(source);
        return decodeMessage(message);
    }

    protected abstract WialonPackage decodeMessage(final String message);

    private String removePostfix(final String source) {
        final int startIndex = source.length() - WialonPackage.POSTFIX.length();
        return source.substring(0, startIndex);
    }
}
