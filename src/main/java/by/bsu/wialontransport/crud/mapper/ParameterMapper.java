package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ParameterMapper extends AbstractMapper<ParameterEntity, Parameter> {

    public ParameterMapper(final ModelMapper modelMapper) {
        super(modelMapper, ParameterEntity.class, Parameter.class);
    }

    @Override
    protected Parameter createDto(final ParameterEntity entity) {
        return new Parameter(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getValue()
        );
    }
}
