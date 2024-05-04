package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder<PACKAGE extends WialonPackage> extends PackageStringDecoder<PACKAGE> {

    public WialonPackageDecoder(final String packagePrefix) {
        super(packagePrefix);
    }

    @Override
    public final PACKAGE decode(final String source) {
        final String message = extractMessage(source);
        return decodeMessage(message);
    }

    protected abstract PACKAGE decodeMessage(final String message);

    private String extractMessage(final String source) {
        final int startIndex = getPrefix().length();
        final int nextEndIndex = source.length() - POSTFIX.length();
        return source.substring(startIndex, nextEndIndex);
    }
}
