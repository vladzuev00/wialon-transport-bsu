package by.vladzuev.locationreceiver.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Tracker implements Dto<Long> {
    Long id;
    String imei;
    String password;
    String phoneNumber;
    User user;
    Mileage mileage;
}
