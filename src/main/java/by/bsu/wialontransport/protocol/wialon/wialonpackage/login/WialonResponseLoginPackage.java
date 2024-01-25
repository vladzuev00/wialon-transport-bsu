package by.bsu.wialontransport.protocol.wialon.wialonpackage.login;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonResponseLoginPackage extends WialonPackage {
    private static final String PREFIX = "#AL#";

    private final Status status;

    @Override
    public String findPrefix() {
        return PREFIX;
    }

    public enum Status {
        NOT_DEFINED("not defined"),
        SUCCESS_AUTHORIZATION("1"),
        CONNECTION_FAILURE("0"),
        ERROR_CHECK_PASSWORD("01");

        private final String value;

        Status(final String value) {
            this.value = value;
        }

        public final String getValue() {
            return this.value;
        }

        public static Status findByValue(final String value) {
            return stream(Status.values())
                    .filter(status -> Objects.equals(status.value, value))
                    .findAny()
                    .orElse(NOT_DEFINED);
        }
    }
}
