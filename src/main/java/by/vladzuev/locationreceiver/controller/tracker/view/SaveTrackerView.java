package by.vladzuev.locationreceiver.controller.tracker.view;

import by.vladzuev.locationreceiver.validation.annotation.ExistingUserId;
import by.vladzuev.locationreceiver.validation.annotation.Password;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

import static java.util.Optional.empty;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class SaveTrackerView extends TrackerView {

    @Password
    private final String password;

    @ExistingUserId
    private final Long userId;

    @Builder
    @JsonCreator
    public SaveTrackerView(@JsonProperty("imei") final String imei,
                           @JsonProperty("phoneNumber") final String phoneNumber,
                           @JsonProperty("password") final String password,
                           @JsonProperty("userId") final Long userId) {
        super(imei, phoneNumber);
        this.password = password;
        this.userId = userId;
    }

    @Override
    public Optional<Long> findId() {
        return empty();
    }
}
