package by.bsu.wialontransport.controller.tracker.view;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TrackerView {
    Long id;
    String imei;
    String phoneNumber;
}
