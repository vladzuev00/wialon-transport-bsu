package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.validation.annotation.Imei;
import by.bsu.wialontransport.validation.annotation.Password;
import by.bsu.wialontransport.validation.annotation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
public class AddedTrackerView {

    @Imei
    String imei;

    @Password
    String password;

    @PhoneNumber
    String phoneNumber;

    @Builder
    @JsonCreator
    public AddedTrackerView(@JsonProperty("imei") final String imei,
                            @JsonProperty("password") final String password,
                            @JsonProperty("phoneNumber") final String phoneNumber) {
        this.imei = imei;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
