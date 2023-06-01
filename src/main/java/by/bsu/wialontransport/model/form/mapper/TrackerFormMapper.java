package by.bsu.wialontransport.model.form.mapper;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.form.TrackerForm;
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
        return new Tracker(
                trackerForm.getId(),
                trackerForm.getImei(),
                trackerForm.getPassword(),
                trackerForm.getPhoneNumber(),
                user
        );
    }

}
