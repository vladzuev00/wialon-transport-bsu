package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.CityEntity;
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
                this.mapAddress(entity),
                this.mapSearchingCitiesProcess(entity)
        );
    }

    @Override
    protected void mapSpecificFields(final City source, final CityEntity destination) {

    }

    private Address mapAddress(final CityEntity source) {
        return super.mapLazyProperty(source, CityEntity::getAddress, Address.class);
    }

    private SearchingCitiesProcess mapSearchingCitiesProcess(final CityEntity source) {
        return super.mapLazyProperty(source, CityEntity::getSearchingCitiesProcess, SearchingCitiesProcess.class);
    }
}
