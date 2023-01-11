package by.bsu.wialontransport.protocol.wialon.wialonpackage.reduceddata;

import lombok.Value;

import static java.lang.Byte.MIN_VALUE;
import static java.util.Arrays.stream;

@Value
public class ResponseReducedDataPackage {
    public static final String PREFIX = "#ASD#";

    Status status;

    public enum Status {
        NOT_DEFINED(MIN_VALUE), ERROR_PACKAGE_STRUCTURE((byte) -1), INCORRECT_TIME((byte) 0),
        PACKAGE_FIX_SUCCESS((byte) 1), ERROR_GETTING_COORDINATE((byte) 10),
        ERROR_GETTING_SPEED_OR_COURSE_OR_HEIGHT((byte) 11),
        ERROR_GETTING_AMOUNT_SATELLITES((byte) 12);

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
