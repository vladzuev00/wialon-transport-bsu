package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import lombok.Getter;

@Getter
public final class NewWingLoginPackage extends NewWingRequestPackage implements LoginPackage {
    private final String imei;

    public NewWingLoginPackage(final int checksum, final String imei) {
        super(checksum);
        this.imei = imei;
    }
}
