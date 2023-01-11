package by.bsu.wialontransport.protocol.wialon.wialonpackage.login;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.Value;

import java.util.Objects;

import static java.util.Arrays.stream;

@Value
public class ResponseLoginPackage implements Package {
    public static final String PREFIX = "#AL#";

    Status status;

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
