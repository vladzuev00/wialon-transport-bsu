package by.bsu.wialontransport.protocol.newwing.model.packages.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.LoginNewWingPackage;
import lombok.Setter;

@Setter
public final class LoginNewWingPackageBuilder extends NewWingRequestPackageBuilder<LoginNewWingPackage> {
    private String imei;

    @Override
    protected LoginNewWingPackage build(final int checksum) {
        return new LoginNewWingPackage(checksum, this.imei);
    }
}