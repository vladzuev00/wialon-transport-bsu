package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.FixStringPrefixedTextPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder extends FixStringPrefixedTextPackageDecoder {

    public WialonPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final WialonPackage decodeWithoutPrefix(final String content) {
        final String message = removePostfix(content);
        return decodeMessage(message);
    }

    protected abstract WialonPackage decodeMessage(final String message);

    private String removePostfix(final String content) {
        final int startPostfixIndex = content.length() - POSTFIX.length();
        return content.substring(0, startPostfixIndex);
    }
}
