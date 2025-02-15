package by.vladzuev.locationreceiver.protocol.jt808.model;

import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import lombok.Value;

@Value
public class JT808RegistrationPackage implements LoginPackage {
    String imei;
}
