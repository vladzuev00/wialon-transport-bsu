package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.validation.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

//TODO: test unique constraint
@Value
public class SaveTrackerView {

    @Imei
    @UniqueTrackerImei
    String imei;

    @Password
    String password;

    @PhoneNumber
    @UniqueTrackerPhoneNumber
    String phoneNumber;

    @NotNull
    Long userId;

    @Builder
    @JsonCreator
    public SaveTrackerView(@JsonProperty("imei") final String imei,
                           @JsonProperty("password") final String password,
                           @JsonProperty("phoneNumber") final String phoneNumber,
                           @JsonProperty("userId") final Long userId) {
        this.imei = imei;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }
}
