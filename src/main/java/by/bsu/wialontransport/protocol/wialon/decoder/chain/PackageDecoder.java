package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.AbstractPackageDeserializer;
import by.bsu.wialontransport.protocol.wialon.decoder.chain.exception.NoSuitablePackageDecoderException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public abstract class PackageDecoder {
    private final PackageDecoder nextDecoder;
    private final String packagePrefix;
    private final AbstractPackageDeserializer packageDeserializer;

    public PackageDecoder(final PackageDecoder nextDecoder, final String packagePrefix,
                          final AbstractPackageDeserializer packageDeserializer) {
        this.nextDecoder = nextDecoder;
        this.packagePrefix = packagePrefix;
        this.packageDeserializer = packageDeserializer;
    }

    public final WialonPackage decode(final String decoded) {
        if (this.isAbleToDecode(decoded)) {
            return this.decodeIndependently(decoded);
        }
        return this.delegateToNextDecoder(decoded);
    }

    private boolean isAbleToDecode(final String decoded) {
        //packagePrefix == null in starter and finisher decoding chain
        return this.packagePrefix != null && decoded.startsWith(this.packagePrefix);
    }

    private WialonPackage decodeIndependently(final String decoded) {
        return this.packageDeserializer.deserialize(decoded);
    }

    private WialonPackage delegateToNextDecoder(final String decoded) {
        if (this.nextDecoder != null) {
            return this.nextDecoder.decode(decoded);
        }
        throw new NoSuitablePackageDecoderException();
    }
}
