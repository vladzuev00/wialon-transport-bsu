package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class DataCalculationsMapper extends AbstractMapper<DataCalculationsEntity, DataCalculations> {

    public DataCalculationsMapper(final ModelMapper modelMapper) {
        super(modelMapper, DataCalculationsEntity.class, DataCalculations.class);
    }

    @Override
    protected DataCalculations createDto(final DataCalculationsEntity entity) {
        return new DataCalculations(
                entity.getId(),
                entity.getGpsOdometer(),
                entity.isIgnitionOn(),
                entity.getEngineOnDurationSeconds(),
                null
        );
    }
}
