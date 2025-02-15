package by.vladzuev.locationreceiver.controller.tracker.view;

import by.vladzuev.locationreceiver.controller.abstraction.View;
import by.vladzuev.locationreceiver.validation.annotation.Imei;
import by.vladzuev.locationreceiver.validation.annotation.PhoneNumber;
import by.vladzuev.locationreceiver.validation.annotation.UniqueTrackerImei;
import by.vladzuev.locationreceiver.validation.annotation.UniqueTrackerPhoneNumber;
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
