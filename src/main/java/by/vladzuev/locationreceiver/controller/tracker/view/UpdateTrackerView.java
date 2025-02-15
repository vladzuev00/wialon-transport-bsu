package by.vladzuev.locationreceiver.controller.tracker.view;

import by.vladzuev.locationreceiver.validation.annotation.ExistingTrackerId;
import by.vladzuev.locationreceiver.validation.annotation.Password;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UpdateTrackerView extends TrackerView {

    @ExistingTrackerId
    private final Long id;

    @Password
    private final String password;

    @Builder
    @JsonCreator
    public UpdateTrackerView(@JsonProperty("imei") final String imei,
                             @JsonProperty("phoneNumber") final String phoneNumber,
                             @JsonProperty("id") final Long id,
                             @JsonProperty("password") final String password) {
        super(imei, phoneNumber);
        this.id = id;
        this.password = password;
    }

    @Override
    public Optional<Long> findId() {
        return Optional.of(id);
    }
}
