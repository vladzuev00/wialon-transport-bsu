package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.mapper.SearchingCitiesProcessMapper;
import by.bsu.wialontransport.crud.repository.SearchingCitiesProcessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchingCitiesProcessService extends CRUDService<
        Long,
        SearchingCitiesProcessEntity,
        SearchingCitiesProcess,
        SearchingCitiesProcessMapper,
        SearchingCitiesProcessRepository
        > {

    public SearchingCitiesProcessService(final SearchingCitiesProcessMapper mapper,
                                         final SearchingCitiesProcessRepository repository) {
        super(mapper, repository);
    }

    public int updateStatus(final SearchingCitiesProcess process, final Status newStatus) {
        return findInt(
                repository -> repository.updateStatus(
                        process.getId(),
                        newStatus
                )
        );
    }

    public int increaseHandledPoints(final SearchingCitiesProcess process, final long delta) {
        return findInt(
                repository -> repository.increaseHandledPoints(
                        process.getId(),
                        delta
                )
        );
    }

    @Transactional(readOnly = true)
    public Page<SearchingCitiesProcess> findByStatus(final Status status, final PageRequest pageRequest) {
        return findDtoPage(repository -> repository.findByStatus(status, pageRequest));
    }

    @Override
    protected void configureBeforeSave(final SearchingCitiesProcessEntity entity) {

    }
}
