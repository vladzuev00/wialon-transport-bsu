package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.Package.POSTFIX;

@FunctionalInterface
public interface PackageDeserializer {

    Package deserialize(String deserialized);

    static String removePrefix(String source) {
        final int indexAfterPrefix = POSTFIX.length();
        return source.substring(indexAfterPrefix);
    }
}
