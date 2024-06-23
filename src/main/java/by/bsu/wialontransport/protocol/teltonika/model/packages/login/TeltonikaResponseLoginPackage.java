package by.bsu.wialontransport.protocol.teltonika.model.packages.login;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TeltonikaResponseLoginPackage implements Package {
    public static final TeltonikaResponseLoginPackage SUCCESS_RESPONSE = new TeltonikaResponseLoginPackage((byte) 1);
    public static final TeltonikaResponseLoginPackage FAIL_RESPONSE = new TeltonikaResponseLoginPackage((byte) 0);

    byte value;
}
