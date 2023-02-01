package by.bsu.wialontransport.protocol.wialon.parameter;

import java.util.Set;

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
}
