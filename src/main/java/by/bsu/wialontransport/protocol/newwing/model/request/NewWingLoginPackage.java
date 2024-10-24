package by.bsu.wialontransport.protocol.newwing.model.request;

import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class NewWingLoginPackage extends NewWingRequestPackage implements LoginPackage {
    private final String imei;

    public NewWingLoginPackage(final int checksum, final String imei) {
        super(checksum);
        this.imei = imei;
    }
}
