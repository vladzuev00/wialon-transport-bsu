package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class FixPrefixedPackageDecoder<SOURCE, PREFIX> extends PrefixedPackageDecoder<SOURCE, PREFIX> {
    private final PREFIX prefix;

    @Override
    public final Package decode(final SOURCE source) {
        final SOURCE sourceWithoutPrefix = removePrefix(source);
        return decodeWithoutPrefix(sourceWithoutPrefix);
    }

    @Override
    protected final boolean isSuitablePrefix(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }

    protected abstract SOURCE removePrefix(final SOURCE source);

    protected abstract Package decodeWithoutPrefix(final SOURCE source);
}
