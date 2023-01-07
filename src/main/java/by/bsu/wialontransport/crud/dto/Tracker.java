package by.bsu.wialontransport.crud.dto;

import lombok.Value;

@Value
public class Tracker implements AbstractDto<Long> {
    Long id;
    String imei;
    String password;
    String phoneNumber;
}
