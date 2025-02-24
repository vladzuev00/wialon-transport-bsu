package by.vladzuev.locationreceiver.crud.mapper.temp;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.crud.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ParameterMapper extends Mapper<ParameterEntity, Parameter> {

    public ParameterMapper(final ModelMapper modelMapper) {
        super(modelMapper, ParameterEntity.class, Parameter.class);
    }

    @Override
    protected Parameter createDto(final ParameterEntity source) {
        return null;
//        return new Parameter(
//                source.getId(),
//                source.getName(),
//                source.getType(),
//                source.getValue()
//        );
    }

    @Override
    protected void mapSpecificFields(final Parameter source, final ParameterEntity destination) {

    }
}
