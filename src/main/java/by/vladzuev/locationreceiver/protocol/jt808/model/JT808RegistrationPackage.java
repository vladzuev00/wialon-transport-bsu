package by.vladzuev.locationreceiver.protocol.jt808.model;

import lombok.Value;

@Value
public class JT808RegistrationPackage {
    String phoneNumber;
    String manufacturerId;
}
