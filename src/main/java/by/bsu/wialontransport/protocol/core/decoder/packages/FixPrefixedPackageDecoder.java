package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class FixPrefixedPackageDecoder<PACKAGE extends Package, PREFIX>
        extends PrefixiedPackageDecoder<PACKAGE, PREFIX> {
    private final PREFIX prefix;

    @Override
    protected final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
