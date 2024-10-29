package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringTextPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder extends PrefixedByStringTextPackageDecoder {

    public WialonPackageDecoder(final String requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final WialonPackage decodeInternal(final String source) {
        final String message = removePostfix(source);
        return decodeMessage(message);
    }

    protected abstract WialonPackage decodeMessage(final String message);

    private String removePostfix(final String source) {
        final int startIndex = source.length() - POSTFIX.length();
        return source.substring(0, startIndex);
    }
}
