package by.bsu.wialontransport.protocol.core.model.login;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ProtectedLoginPackage extends LoginPackage {
    private final String password;

    public ProtectedLoginPackage(final String imei, final String password) {
        super(imei);
        this.password = password;
    }
}
