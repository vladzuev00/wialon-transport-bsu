package by.bsu.wialontransport.crud.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

//@Component
//public final class ExtendedDataMapper extends AbstractDataMapper<ExtendedDataEntity, ExtendedData> {
//
//    public ExtendedDataMapper(final ModelMapper modelMapper) {
//        super(modelMapper, ExtendedDataEntity.class, ExtendedData.class);
//    }
//
//    @Override
//    protected ExtendedData createDto(final ExtendedDataEntity entity) {
//        return new ExtendedData(
//                entity.getId(), entity.getDate(), entity.getTime(), mapLatitude(entity), mapLongitude(entity),
//                entity.getSpeed(), entity.getCourse(), entity.getHeight(), entity.getAmountOfSatellites(),
//                entity.getReductionPrecision(), entity.getInputs(), entity.getOutputs(), entity.getAnalogInputs(),
//                entity.getDriverKeyCode(), null
//        );
//    }
//}
