package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public abstract class AbstractPackageDeserializer {
    private final String prefix;

    public AbstractPackageDeserializer(final String prefix) {
        this.prefix = prefix;
    }

    public final WialonPackage deserialize(String deserialized) {
        final String message = this.removePrefix(deserialized);
        return this.deserializeByMessage(message);
    }

    protected abstract WialonPackage deserializeByMessage(final String message);

    private String removePrefix(final String source) {
        final int indexAfterPrefix = this.prefix.length();
        return source.substring(indexAfterPrefix);
    }
}
