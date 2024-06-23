package by.bsu.wialontransport.protocol.core.decoder.packages;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class FixedPrefixPackageDecoder<PREFIX> implements PackageDecoder<PREFIX> {
    private final PREFIX prefix;

    @Override
    public final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
