package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.Mileage;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMileageMapper extends Mapper<MileageEntity, Mileage> {

    public TrackerMileageMapper(final ModelMapper modelMapper) {
        super(modelMapper, MileageEntity.class, Mileage.class);
    }

    @Override
    protected Mileage createDto(final MileageEntity source) {
        return new Mileage(
                source.getId(),
                source.getUrban(),
                source.getCountry()
        );
    }

    @Override
    protected void mapSpecificFields(final Mileage source, final MileageEntity destination) {

    }
}
