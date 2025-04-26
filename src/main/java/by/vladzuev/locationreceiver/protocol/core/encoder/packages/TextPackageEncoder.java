package by.vladzuev.locationreceiver.protocol.core.encoder.packages;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class TextPackageEncoder<PACKAGE> extends PackageEncoder<PACKAGE> {

    public TextPackageEncoder(final Class<PACKAGE> responseType) {
        super(responseType);
    }

    @Override
    protected final byte[] encodeInternal(final PACKAGE response) {
        return getString(response).getBytes(US_ASCII);
    }

    protected abstract String getString(final PACKAGE response);
}
