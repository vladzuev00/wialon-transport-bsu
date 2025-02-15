package by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class WialonResponseSingleLocationPackage implements WialonPackage {
    public static final String PREFIX = "#AD#";

    private final Status status;

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        PACKAGE_FIX_SUCCESS((byte) 1);

        private final byte value;
    }
}
