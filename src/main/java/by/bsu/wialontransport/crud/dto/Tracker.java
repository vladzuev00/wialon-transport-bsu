package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Tracker implements AbstractDto<Long> {
    Long id;
    String imei;
    String password;
    String phoneNumber;
    User user;
    TrackerMileage trackerOdometer;
}
