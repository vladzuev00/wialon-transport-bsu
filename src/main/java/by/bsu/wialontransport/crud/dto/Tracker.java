package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Value
@AllArgsConstructor
@Builder
public class Tracker implements Dto<Long> {
    Long id;
    String imei;
    String password;
    String phoneNumber;
    User user;
    TrackerMileage mileage;
    Data lastData;

    public Optional<Data> findLastData() {
        return ofNullable(lastData);
    }
}
