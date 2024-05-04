package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder extends PackageStringDecoder {

    public WialonPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    public final WialonPackage decode(final String source) {
        final String message = getMessage(source);
        return decodeMessage(message);
    }

    protected abstract WialonPackage decodeMessage(final String message);

    private String getMessage(final String source) {
        final int startIndex = getPrefix().length();
        final int nextEndIndex = source.length() - POSTFIX.length();
        return source.substring(startIndex, nextEndIndex);
    }
}
