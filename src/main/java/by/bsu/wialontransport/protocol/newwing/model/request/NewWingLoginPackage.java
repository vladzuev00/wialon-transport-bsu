package by.bsu.wialontransport.protocol.newwing.model.request;

import by.bsu.wialontransport.protocol.core.model.LoginPackage;
import lombok.Value;

@Value
public class NewWingLoginPackage implements LoginPackage {
    String imei;
}
