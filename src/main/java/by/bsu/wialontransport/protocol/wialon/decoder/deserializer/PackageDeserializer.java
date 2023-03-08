package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;

@FunctionalInterface
public interface PackageDeserializer {

    Package deserialize(String deserialized);

    static String removePrefix(final String source, final String prefix) {
        final int indexAfterPrefix = prefix.length();
        return source.substring(indexAfterPrefix);
    }
}
