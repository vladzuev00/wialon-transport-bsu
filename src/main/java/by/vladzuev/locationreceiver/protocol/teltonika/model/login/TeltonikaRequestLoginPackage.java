package by.vladzuev.locationreceiver.protocol.teltonika.model.login;

import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import lombok.Value;

@Value
public class TeltonikaRequestLoginPackage implements LoginPackage {
    String imei;
}
