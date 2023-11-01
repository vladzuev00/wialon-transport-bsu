package by.bsu.wialontransport.protocol.newwing.model.packages;

import lombok.Getter;

@Getter
public final class LoginNewWingPackage extends NewWingPackage {
    private final String imei;

    public LoginNewWingPackage(final int checksum, final String imei) {
        super(checksum);
        this.imei = imei;
    }
}