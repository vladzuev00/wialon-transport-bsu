package by.bsu.wialontransport.protocol.core.decoder.packages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class FixPrefixedPackageDecoder<PREFIX> extends PrefixiedPackageDecoder<PREFIX> {
    private final PREFIX prefix;

    @Override
    protected final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
