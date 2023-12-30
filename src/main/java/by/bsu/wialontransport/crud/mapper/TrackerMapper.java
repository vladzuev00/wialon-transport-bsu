package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMapper extends Mapper<TrackerEntity, Tracker> {

    public TrackerMapper(final ModelMapper modelMapper) {
        super(modelMapper, TrackerEntity.class, Tracker.class);
    }

    @Override
    protected Tracker createDto(final TrackerEntity source) {
        return new Tracker(
                source.getId(),
                source.getImei(),
                source.getPassword(),
                source.getPhoneNumber(),
                mapUser(source),
                mapMileage(source),
                mapLastData(source)
        );
    }

    @Override
    protected void mapSpecificFields(final Tracker source, final TrackerEntity destination) {

    }

    private User mapUser(final TrackerEntity source) {
        return mapLazy(source.getUser(), User.class);
    }

    private TrackerMileage mapMileage(final TrackerEntity source) {
        return mapLazy(source.getMileage(), TrackerMileage.class);
    }

    private Data mapLastData(final TrackerEntity source) {
        return mapLazy(source.getLastData(), Data.class);
    }
}
