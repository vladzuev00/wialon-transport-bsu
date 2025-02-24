package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.City;
import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CityCrudMapper extends CrudMapper<City, CityEntity> {

}
