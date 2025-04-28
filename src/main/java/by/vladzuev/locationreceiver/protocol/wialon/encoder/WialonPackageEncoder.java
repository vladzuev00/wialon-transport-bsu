package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.TextPackageEncoder;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;

public abstract class WialonPackageEncoder<PACKAGE extends WialonPackage> extends TextPackageEncoder<PACKAGE> {

    public WialonPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final String getText(final PACKAGE response) {
        return response.getPrefix() + encodeMessage(response) + WialonPackage.POSTFIX;
    }

    protected abstract String encodeMessage(final PACKAGE response);
}
