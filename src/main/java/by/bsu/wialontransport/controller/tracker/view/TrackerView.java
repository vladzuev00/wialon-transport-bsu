package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.controller.abstraction.View;
import by.bsu.wialontransport.validation.annotation.Imei;
import by.bsu.wialontransport.validation.annotation.PhoneNumber;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerImei;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerPhoneNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@UniqueTrackerImei
@UniqueTrackerPhoneNumber
public abstract class TrackerView implements View<Long> {

    @Imei
    private final String imei;

    @PhoneNumber
    private final String phoneNumber;
}
