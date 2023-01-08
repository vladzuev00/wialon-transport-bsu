package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class DataMapper extends AbstractDataMapper<DataEntity, Data> {

    public DataMapper(final ModelMapper modelMapper) {
        super(modelMapper, DataEntity.class, Data.class);
    }

    @Override
    protected Data createDto(final DataEntity entity) {
        return new Data(entity.getId(), entity.getDate(), entity.getTime(), mapLatitude(entity), mapLongitude(entity),
                entity.getSpeed(), entity.getCourse(), entity.getHeight(), entity.getAmountOfSatellites());
    }
}
