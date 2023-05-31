package by.bsu.wialontransport.model.form;

import lombok.Value;

@Value
public class TrackerForm {
    Long id;
    String imei;
    String password;
    String phoneNumber;
}
