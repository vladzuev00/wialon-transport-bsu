package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class PackageDecoder<PREFIX, SOURCE> {
    private final PREFIX prefix;

    public final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }

    public abstract Package decode(final SOURCE source);
}
