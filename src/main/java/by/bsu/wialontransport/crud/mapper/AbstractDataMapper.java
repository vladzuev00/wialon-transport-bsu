package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.modelmapper.ModelMapper;

public abstract class AbstractDataMapper<DataEntityType extends DataEntity, DataDtoType extends Data>
        extends AbstractMapper<DataEntityType, DataDtoType> {

    public AbstractDataMapper(final ModelMapper modelMapper, final Class<DataEntityType> entityType,
                              final Class<DataDtoType> dtoType) {
        super(modelMapper, entityType, dtoType);
    }

    @Override
    protected final void mapSpecificFields(final Data source, final DataEntity destination) {
        destination.setLatitude(mapLatitude(source));
        destination.setLongitude(mapLongitude(source));
    }

    protected static Data.Latitude mapLatitude(final DataEntity source) {
        final DataEntity.Latitude mappedLatitude = source.getLatitude();
        return new Data.Latitude(mappedLatitude.getDegrees(), mappedLatitude.getMinutes(),
                mappedLatitude.getMinuteShare(), mappedLatitude.getType());
    }

    protected static Data.Longitude mapLongitude(final DataEntity source) {
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
