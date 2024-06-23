package by.bsu.wialontransport.protocol.teltonika.model.packages.login;

import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import lombok.Value;

@Value
public class TeltonikaRequestLoginPackage implements LoginPackage {
    String imei;
}
