package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
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
                entity.getBoundingBox(),
                entity.getCenter(),
                entity.getCityName(),
                entity.getCountryName(),
                entity.getGeometry(),
                this.mapSearchingCitiesProcess(entity)
        );
    }

    private SearchingCitiesProcess mapSearchingCitiesProcess(final CityEntity source) {
        final SearchingCitiesProcessEntity mapped = source.getSearchingCitiesProcess();
        return super.mapPropertyIfLoadedOrElseNull(mapped, SearchingCitiesProcess.class);
    }
}
