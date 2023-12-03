package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class SearchingCitiesProcessMapper
        extends Mapper<SearchingCitiesProcessEntity, SearchingCitiesProcess> {

    public SearchingCitiesProcessMapper(final ModelMapper modelMapper) {
        super(modelMapper, SearchingCitiesProcessEntity.class, SearchingCitiesProcess.class);
    }

    @Override
    protected SearchingCitiesProcess createDto(final SearchingCitiesProcessEntity entity) {
        return new SearchingCitiesProcess(
                entity.getId(),
                entity.getBounds(),
                entity.getSearchStep(),
                entity.getTotalPoints(),
                entity.getHandledPoints(),
                entity.getStatus()
        );
    }
}
