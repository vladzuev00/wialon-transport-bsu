package by.bsu.wialontransport.protocol.wialon.wialonpackage.login;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonRequestLoginPackage extends WialonPackage {
    private static final String PREFIX = "#L#";

    String imei;
    String password;

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
