package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonRequestLoginPackage extends WialonPackage {
    public static final String PREFIX = "#L#";

    private final String imei;
    private final String password;

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
