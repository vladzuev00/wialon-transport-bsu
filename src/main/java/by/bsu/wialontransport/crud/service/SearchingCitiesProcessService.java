package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import by.bsu.wialontransport.crud.mapper.SearchingCitiesProcessMapper;
import by.bsu.wialontransport.crud.repository.SearchingCitiesProcessRepository;
import org.springframework.stereotype.Service;

@Service
public class SearchingCitiesProcessService extends AbstractCRUDService<
        Long,
        SearchingCitiesProcessEntity,
        SearchingCitiesProcess,
        SearchingCitiesProcessMapper,
        SearchingCitiesProcessRepository> {

    public SearchingCitiesProcessService(final SearchingCitiesProcessMapper mapper,
                                         final SearchingCitiesProcessRepository repository) {
        super(mapper, repository);
    }
}
