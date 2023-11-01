package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class PackageDecoder<PREFIX, SOURCE, PACKAGE extends Package> {
    private final PREFIX packagePrefix;

    public final boolean isAbleToDecode(final PREFIX packagePrefix) {
        return Objects.equals(packagePrefix, this.packagePrefix);
    }

    public abstract PACKAGE decode(final SOURCE source);
}
