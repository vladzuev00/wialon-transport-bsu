package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMileageMapper extends AbstractMapper<TrackerMileageEntity, TrackerMileage> {

    public TrackerMileageMapper(final ModelMapper modelMapper) {
        super(modelMapper, TrackerMileageEntity.class, TrackerMileage.class);
    }

    @Override
    protected TrackerMileage createDto(final TrackerMileageEntity entity) {
        return new TrackerMileage(
                entity.getId(),
                entity.getUrban(),
                entity.getCountry()
        );
    }
}
