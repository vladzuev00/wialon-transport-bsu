package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import lombok.Setter;

@Setter
public final class LoginNewWingPackageBuilder extends NewWingRequestPackageBuilder<NewWingLoginPackage> {
    private String imei;

    @Override
    protected NewWingLoginPackage build(final int checksum) {
        return new NewWingLoginPackage(checksum, this.imei);
    }
}