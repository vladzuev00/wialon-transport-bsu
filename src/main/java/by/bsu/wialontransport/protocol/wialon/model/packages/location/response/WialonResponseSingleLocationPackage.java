package by.bsu.wialontransport.protocol.wialon.model.packages.location.response;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonResponseSingleLocationPackage extends WialonPackage {
    public static final String PREFIX = "#AD#";

    private final Status status;

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        WRONG_PACKAGE_STRUCTURE((byte) -1),
        WRONG_DATE_TIME((byte) 0),
        PACKAGE_FIX_SUCCESS((byte) 1),
        WRONG_COORDINATE((byte) 10),
        WRONG_SPEED_COURSE_ALTITUDE((byte) 11),
        WRONG_SATELLITE_COUNT_HDOP((byte) 12),
        WRONG_INPUTS_OUTPUTS((byte) 13),
        WRONG_ANALOG_INPUTS((byte) 14),
        WRONG_PARAMETERS((byte) 15);

        private final byte value;
    }
}
