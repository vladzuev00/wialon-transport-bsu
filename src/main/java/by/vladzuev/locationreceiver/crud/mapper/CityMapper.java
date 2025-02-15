package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.City;
import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper extends Mapper<CityEntity, City> {

    public CityMapper(final ModelMapper modelMapper) {
        super(modelMapper, CityEntity.class, City.class);
    }

    @Override
    protected City createDto(final CityEntity entity) {
        return new City(
                entity.getId(),
                mapAddress(entity),
                mapSearchingCitiesProcess(entity)
        );
    }

    @Override
    protected void mapSpecificFields(final City source, final CityEntity destination) {

    }

    private Address mapAddress(final CityEntity source) {
        return mapLazy(source.getAddress(), Address.class);
    }

    private SearchingCitiesProcess mapSearchingCitiesProcess(final CityEntity source) {
        return mapLazy(source.getSearchingCitiesProcess(), SearchingCitiesProcess.class);
    }
}
