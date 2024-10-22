package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

@Value
public class JT808RegistrationMessage {
    String phoneNumber;
    String manufacturerId;
}
