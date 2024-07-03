package by.bsu.wialontransport.protocol.core.decoder.packages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class FixPrefixedPackageDecoder<SOURCE, PREFIX> extends PrefixedPackageDecoder<SOURCE, PREFIX> {
    private final PREFIX prefix;

    @Override
    protected final boolean isSuitablePrefix(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
