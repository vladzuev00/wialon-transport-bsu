package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMapper extends AbstractMapper<TrackerEntity, Tracker> {

    public TrackerMapper(final ModelMapper modelMapper) {
        super(modelMapper, TrackerEntity.class, Tracker.class);
    }

    @Override
    protected Tracker createDto(final TrackerEntity entity) {
        return new Tracker(
                entity.getId(),
                entity.getImei(),
                entity.getPassword(),
                entity.getPhoneNumber()
        );
    }
}
