package by.bsu.wialontransport.protocol.wialon.wialonpackage.message;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.Value;

import static java.util.Arrays.stream;

@Value
public class ResponseMessagePackage implements Package {
    public static final String PREFIX = "#AM#";

    Status status;

    public enum Status {
        NOT_DEFINED((byte) -1), SUCCESS((byte) 1), ERROR((byte) 0);

        private final byte value;

        Status(final byte value) {
            this.value = value;
        }

        public final byte getValue() {
            return this.value;
        }

        public static Status findByValue(final byte value) {
            return stream(Status.values())
                    .filter(status -> status.value == value)
                    .findAny()
                    .orElse(NOT_DEFINED);
        }
    }
}
