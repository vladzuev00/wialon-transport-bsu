package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.*;

import static java.lang.Byte.MIN_VALUE;
import static java.util.Arrays.stream;

@Value
public class WialonResponseDataPackage implements WialonPackage {
    public static final String PREFIX = "#AD#";

    Status status;

    public enum Status {
        NOT_DEFINED(MIN_VALUE), ERROR_PACKAGE_STRUCTURE((byte) -1), INCORRECT_TIME((byte) 0),
        PACKAGE_FIX_SUCCESS((byte) 1), ERROR_GETTING_COORDINATE((byte) 10),
        ERROR_GETTING_SPEED_OR_COURSE_OR_HEIGHT((byte) 11),
        ERROR_GETTING_AMOUNT_SATELLITES_OR_HDOP((byte) 12),
        ERROR_GETTING_INPUTS_OR_OUTPUTS((byte) 13), ERROR_GETTING_ANALOG_INPUTS((byte) 14),
        ERROR_GETTING_PARAMETERS((byte) 15);

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
