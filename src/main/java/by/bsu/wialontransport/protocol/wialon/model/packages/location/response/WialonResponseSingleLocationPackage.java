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
        PACKAGE_FIX_SUCCESS((byte) 1);

        private final byte value;
    }
}
