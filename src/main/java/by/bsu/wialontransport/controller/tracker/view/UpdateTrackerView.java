package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.controller.abstraction.DtoRequestView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.validation.annotation.Imei;
import by.bsu.wialontransport.validation.annotation.Password;
import by.bsu.wialontransport.validation.annotation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class UpdateTrackerView implements DtoRequestView<Tracker> {

    @NotNull
    Long id;

    @Imei
    String imei;

    @Password
    String password;

    @PhoneNumber
    String phoneNumber;

    @Builder
    @JsonCreator
    public UpdateTrackerView(@JsonProperty("id") final Long id,
                             @JsonProperty("imei") final String imei,
                             @JsonProperty("password") final String password,
                             @JsonProperty("phoneNumber") final String phoneNumber) {
        this.id = id;
        this.imei = imei;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Tracker createDto() {
        return Tracker.builder()
                .id(id)
                .imei(imei)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }
}
