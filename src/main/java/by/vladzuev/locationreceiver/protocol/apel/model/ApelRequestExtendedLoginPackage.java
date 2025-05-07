package by.vladzuev.locationreceiver.protocol.apel.model;

import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import lombok.Value;

@Value
public class ApelRequestExtendedLoginPackage implements ProtectedLoginPackage {
    String imei;
    String password;
}
