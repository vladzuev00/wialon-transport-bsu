package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage.POSTFIX;

public abstract class WialonPackageDecoder<PACKAGE extends WialonPackage> extends PackageStringDecoder<PACKAGE> {

    public WialonPackageDecoder(final String packagePrefix) {
        super(packagePrefix);
    }

    @Override
    public final PACKAGE decode(final String source) {
        final String message = this.extractMessage(source);
        return this.decodeMessage(message);
    }

    protected abstract PACKAGE decodeMessage(final String message);

    private String extractMessage(final String source) {
        final int startMessageIndex = this.findPackagePrefixLength();
        final int endMessageNextIndex = source.length() - POSTFIX.length();
        return source.substring(startMessageIndex, endMessageNextIndex);
    }

    private int findPackagePrefixLength() {
        final String packagePrefix = super.getPackagePrefix();
        return packagePrefix.length();
    }
}
