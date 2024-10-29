package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringTextPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder extends PrefixedByStringTextPackageDecoder {

    public WialonPackageDecoder(final String requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final WialonPackage decodeInternal(final String content) {
        final String message = removePostfix(content);
        return decodeMessage(message);
    }

    protected abstract WialonPackage decodeMessage(final String message);

    private String removePostfix(final String content) {
        final int startIndex = content.length() - POSTFIX.length();
        return content.substring(0, startIndex);
    }
}
