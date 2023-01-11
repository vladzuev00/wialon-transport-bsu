package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

@FunctionalInterface
public interface PackageDeserializer {
    String PACKAGE_PREFIX = "/r/n";

    Package deserialize(String deserialized);

    static String removePrefix(String source) {
        final int indexAfterPrefix = PACKAGE_PREFIX.length();
        return source.substring(indexAfterPrefix);
    }
}
