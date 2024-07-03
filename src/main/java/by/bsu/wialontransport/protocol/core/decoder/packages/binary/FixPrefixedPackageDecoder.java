package by.bsu.wialontransport.protocol.core.decoder.packages.binary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class FixPrefixedPackageDecoder<PREFIX> extends PrefixedPackageDecoder<PREFIX> {
    private final PREFIX prefix;

    @Override
    protected final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
