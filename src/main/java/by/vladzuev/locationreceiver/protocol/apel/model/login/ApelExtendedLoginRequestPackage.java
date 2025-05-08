package by.vladzuev.locationreceiver.protocol.apel.model.login;

import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import lombok.Value;

@Value
public class ApelExtendedLoginRequestPackage implements ProtectedLoginPackage {
    String imei;
    String password;
}
