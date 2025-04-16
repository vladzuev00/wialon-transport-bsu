//package by.vladzuev.locationreceiver.crud.mapper;
//
//import by.vladzuev.locationreceiver.crud.dto.Parameter;
//import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
//
//@Mapper(componentModel = SPRING)
//public interface ParameterCrudMapper extends CrudMapper<Parameter, ParameterEntity> {
//
//    @Override
//    @Mapping(
//            target = "location",
//            expression = """
//                    java(
//                        dto.getLocation() != null
//                        ? by.vladzuev.locationreceiver.crud.entity.ParameterEntity.builder().id(dto.getId()).build()
//                        : null
//                    )"""
//    )
//    ParameterEntity mapDto(final Parameter dto);
//
//    @Override
//    @Mapping(
//            target = "location",
//            expression = "java(by.vladzuev.locationreceiver.crud.dto.Parameter.builder().id(entity.getId()).build())"
//    )
//    Parameter mapEntity(final ParameterEntity entity);
//}
