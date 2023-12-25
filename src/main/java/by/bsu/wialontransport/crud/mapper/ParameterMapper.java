package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ParameterMapper extends Mapper<ParameterEntity, Parameter> {

    public ParameterMapper(final ModelMapper modelMapper) {
        super(modelMapper, ParameterEntity.class, Parameter.class);
    }

    @Override
    protected Parameter createDto(final ParameterEntity source) {
//        return new Parameter(
//                source.getId(),
//                source.getName(),
//                source.getType(),
//                source.getValue(),
//                this.mapData(source)
//        );
        return null;
    }

    @Override
    protected void mapSpecificFields(final Parameter source, final ParameterEntity destination) {

    }

//    private Data mapData(final ParameterEntity source) {
//        return super.mapLazy(source, ParameterEntity::getData, Data.class);
//    }
}
