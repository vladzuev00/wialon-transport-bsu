package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity.Status;
import by.vladzuev.locationreceiver.crud.mapper.SearchingCitiesProcessMapper;
import by.vladzuev.locationreceiver.crud.repository.SearchingCitiesProcessRepository;
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
    public Page<SearchingCitiesProcess> findByStatusOrderedById(final Status status, final PageRequest pageRequest) {
        return findDtoPage(repository -> repository.findByStatusOrderedById(status, pageRequest));
    }

    @Override
    protected void configureBeforeSave(final SearchingCitiesProcessEntity entity) {

    }
}
