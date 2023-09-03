package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.TrackerOdometer;
import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerOdometerMapper extends AbstractMapper<TrackerOdometerEntity, TrackerOdometer> {

    public TrackerOdometerMapper(final ModelMapper modelMapper) {
        super(modelMapper, TrackerOdometerEntity.class, TrackerOdometer.class);
    }

    @Override
    protected TrackerOdometer createDto(final TrackerOdometerEntity entity) {
        return new TrackerOdometer(
                entity.getId(),
                entity.getUrban(),
                entity.getCountry()
        );
    }
}
