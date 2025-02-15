package by.vladzuev.locationreceiver.controller.tracker.view;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ResponseTrackerView extends TrackerView {
    private final Long id;

    @Builder
    public ResponseTrackerView(final String imei, final String phoneNumber, final Long id) {
        super(imei, phoneNumber);
        this.id = id;
    }

    @Override
    public Optional<Long> findId() {
        return Optional.of(id);
    }
}
