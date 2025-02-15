package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.TrackerMileage;
import by.vladzuev.locationreceiver.crud.entity.TrackerMileageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMileageMapper extends Mapper<TrackerMileageEntity, TrackerMileage> {

    public TrackerMileageMapper(final ModelMapper modelMapper) {
        super(modelMapper, TrackerMileageEntity.class, TrackerMileage.class);
    }

    @Override
    protected TrackerMileage createDto(final TrackerMileageEntity source) {
        return new TrackerMileage(
                source.getId(),
                source.getUrban(),
                source.getCountry()
        );
    }

    @Override
    protected void mapSpecificFields(final TrackerMileage source, final TrackerMileageEntity destination) {

    }
}
