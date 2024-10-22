package by.bsu.wialontransport.protocol.core.decoder.packages;

public interface PackageDecoder<SOURCE> {
    boolean isAbleDecode(final SOURCE source);

    Object decode(final SOURCE source);
}
