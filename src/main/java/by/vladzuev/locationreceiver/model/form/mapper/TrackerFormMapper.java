package by.vladzuev.locationreceiver.model.form.mapper;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.model.form.TrackerForm;
import org.springframework.stereotype.Component;

@Component
public final class TrackerFormMapper {

    public TrackerForm map(final Tracker tracker) {
        return new TrackerForm(
                tracker.getId(),
                tracker.getImei(),
                tracker.getPassword(),
                tracker.getPhoneNumber()
        );
    }

    public Tracker map(final TrackerForm trackerForm, final User user) {
        return Tracker.builder()
                .id(trackerForm.getId())
                .imei(trackerForm.getImei())
                .password(trackerForm.getPassword())
                .phoneNumber(trackerForm.getPhoneNumber())
                .user(user)
                .build();
    }

}
