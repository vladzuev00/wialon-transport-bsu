package by.vladzuev.locationreceiver.protocol.newwing.model.request;

import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import lombok.Value;

@Value
public class NewWingLoginPackage implements LoginPackage {
    String imei;
}
