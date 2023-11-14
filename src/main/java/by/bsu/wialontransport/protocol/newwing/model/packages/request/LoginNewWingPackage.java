package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import lombok.Getter;

@Getter
public final class LoginNewWingPackage extends NewWingRequestPackage implements LoginPackage {
    private final String imei;

    public LoginNewWingPackage(final int checksum, final String imei) {
        super(checksum);
        this.imei = imei;
    }
}
