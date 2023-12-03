package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.mapper.SearchingCitiesProcessMapper;
import by.bsu.wialontransport.crud.repository.SearchingCitiesProcessRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void updateStatus(final SearchingCitiesProcess process, final Status newStatus) {
        super.repository.updateStatus(process.getId(), newStatus);
    }

    public void increaseHandledPoints(final SearchingCitiesProcess process, final long delta) {
        this.repository.increaseHandledPoints(process.getId(), delta);
    }

    @Transactional(readOnly = true)
    public List<SearchingCitiesProcess> findByStatus(final Status status, final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        final List<SearchingCitiesProcessEntity> foundEntities = this.repository.findByStatus(status, pageable);
        return super.mapper.mapToDtos(foundEntities);
    }
}
