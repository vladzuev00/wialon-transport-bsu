package by.bsu.wialontransport.protocol.wialon.model.packages.login;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class WialonResponseLoginPackage implements WialonPackage {
    public static final String PREFIX = "#AL#";

    private final Status status;

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        SUCCESS_AUTHORIZATION("1"),
        CONNECTION_FAILURE("0"),
        PASSWORD_ERROR("01");

        private final String value;
    }
}
