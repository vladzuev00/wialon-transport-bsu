package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.crud.dto.Tracker;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TrackerView {
    Long id;
    String imei;
    String phoneNumber;

    public TrackerView(final Tracker tracker) {
        id = tracker.getId();
        imei = tracker.getImei();
        phoneNumber = tracker.getPhoneNumber();
    }
}
