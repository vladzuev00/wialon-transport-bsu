package by.vladzuev.locationreceiver.protocol.wialon.model.location.response;

import by.vladzuev.locationreceiver.protocol.wialon.model.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class WialonResponseSingleLocationPackage implements WialonPackage {
    public static final String PREFIX = "#AD#";

    private final Status status;

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        PACKAGE_FIX_SUCCESS((byte) 1);

        private final byte value;
    }
}
