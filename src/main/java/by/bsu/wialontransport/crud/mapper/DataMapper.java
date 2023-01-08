package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class DataMapper extends AbstractMapper<DataEntity, Data> {

    public DataMapper(final ModelMapper modelMapper) {
        super(modelMapper, DataEntity.class, Data.class);
    }

    @Override
    protected Data createDto(final DataEntity entity) {
        return new Data(entity.getId(), entity.getDate(), entity.getTime(), mapLatitude(entity), mapLongitude(entity),
                entity.getSpeed(), entity.getCourse(), entity.getCourse(), entity.getHeight());
    }

    @Override
    protected void mapSpecificFields(final Data source, final DataEntity destination) {
        destination.setLatitude(mapLatitude(source));
        destination.setLongitude(mapLongitude(source));
    }

    private static Data.Latitude mapLatitude(final DataEntity source) {
        final DataEntity.Latitude mappedLatitude = source.getLatitude();
        return new Data.Latitude(mappedLatitude.getDegrees(), mappedLatitude.getMinutes(),
                mappedLatitude.getMinuteShare(), mappedLatitude.getType());
    }

    private static Data.Longitude mapLongitude(final DataEntity source) {
        final DataEntity.Longitude mappedLongitude = source.getLongitude();
        return new Data.Longitude(mappedLongitude.getDegrees(), mappedLongitude.getMinutes(),
                mappedLongitude.getMinuteShare(), mappedLongitude.getType());
    }

    private static DataEntity.Latitude mapLatitude(final Data source) {
        final Data.Latitude mappedLatitude = source.getLatitude();
        return new DataEntity.Latitude(mappedLatitude.getDegrees(), mappedLatitude.getMinutes(),
                mappedLatitude.getMinuteShare(), mappedLatitude.getType());
    }

    private static DataEntity.Longitude mapLongitude(final Data source) {
        final Data.Longitude mappedLongitude = source.getLongitude();
        return new DataEntity.Longitude(mappedLongitude.getDegrees(), mappedLongitude.getMinutes(),
                mappedLongitude.getMinuteShare(), mappedLongitude.getType());
    }
}
