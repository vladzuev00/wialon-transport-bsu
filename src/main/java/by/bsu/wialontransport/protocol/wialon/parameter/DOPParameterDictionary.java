package by.bsu.wialontransport.protocol.wialon.parameter;

import java.util.Arrays;
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
}
