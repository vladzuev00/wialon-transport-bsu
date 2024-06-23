package by.bsu.wialontransport.protocol.core.decoder.packages;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class PackageDecoderByFixedPrefix<PREFIX> extends PackageDecoderByPrefix<PREFIX> {
    private final PREFIX prefix;

    @Override
    protected final boolean isAbleToDecode(final PREFIX prefix) {
        return Objects.equals(prefix, this.prefix);
    }
}
