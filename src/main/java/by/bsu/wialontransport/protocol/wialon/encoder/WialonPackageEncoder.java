package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.core.encoder.packages.StringPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage.POSTFIX;

public abstract class WialonPackageEncoder<PACKAGE extends WialonPackage> extends StringPackageEncoder<PACKAGE> {

    public WialonPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final String getString(final PACKAGE response) {
        return response.getPrefix() + encodeMessage(response) + POSTFIX;
    }

    protected abstract String encodeMessage(final PACKAGE response);
}
