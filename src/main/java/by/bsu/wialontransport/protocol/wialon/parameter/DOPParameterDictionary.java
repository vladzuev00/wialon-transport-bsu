package by.bsu.wialontransport.protocol.wialon.parameter;

import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;

public enum DOPParameterDictionary {
    HDOP(Set.of("par122", "122")),
    VDOP(Set.of("par123", "123")),
    PDOP(Set.of("par124", "124"));

    private final Set<String> aliases;

    DOPParameterDictionary(final Set<String> aliases) {
        this.aliases = aliases;
    }

    public Set<String> getAliases() {
        return this.aliases;
    }

    public boolean isAlias(final String research) {
        return this.aliases.contains(research);
    }

    public static Optional<DOPParameterDictionary> findByAlias(final String alias) {
        return stream(values())
                .filter(dictionary -> dictionary.isAlias(alias))
                .findFirst();
    }

    public String findAnyAlias() {
        return this.aliases.stream()
                .findFirst()
                .orElseThrow(DictionaryDoesNotHaveAnyAliasException::new);
    }

    private static final class DictionaryDoesNotHaveAnyAliasException extends RuntimeException {
        public DictionaryDoesNotHaveAnyAliasException() {

        }

        public DictionaryDoesNotHaveAnyAliasException(final String description) {
            super(description);
        }

        public DictionaryDoesNotHaveAnyAliasException(final Exception cause) {
            super(cause);
        }

        public DictionaryDoesNotHaveAnyAliasException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
