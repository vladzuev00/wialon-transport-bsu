package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper extends AbstractMapper<CityEntity, City> {

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

    private Address mapAddress(final CityEntity source) {
        final AddressEntity mapped = source.getAddress();
        return super.mapPropertyIfLoadedOrElseNull(mapped, Address.class);
    }

    private SearchingCitiesProcess mapSearchingCitiesProcess(final CityEntity source) {
        final SearchingCitiesProcessEntity mapped = source.getSearchingCitiesProcess();
        return super.mapPropertyIfLoadedOrElseNull(mapped, SearchingCitiesProcess.class);
    }
}
