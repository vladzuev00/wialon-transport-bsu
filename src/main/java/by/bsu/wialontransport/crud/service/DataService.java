package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.mapper.DataMapper;
import by.bsu.wialontransport.crud.repository.DataRepository;
import by.bsu.wialontransport.model.DateInterval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DataService extends AbstractCRUDService<Long, DataEntity, Data, DataMapper, DataRepository> {

    public DataService(final DataMapper mapper, final DataRepository repository) {
        super(mapper, repository);
    }

    //TODO: refactor tests
    @Transactional(readOnly = true)
    public Optional<Data> findTrackerLastData(final Tracker tracker) {
        final Long trackerId = tracker.getId();
        final Optional<DataEntity> optionalEntity = super.repository.findTrackerLastDataByTrackerId(trackerId);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<Data> findDataWithTrackerAndAddress(final User user, final DateInterval dateInterval) {
        final List<DataEntity> foundEntities = super.repository.findDataWithTrackerAndAddressOfUser(
                user.getId(),
                dateInterval.getStart(),
                dateInterval.getEnd()
        );
        return super.mapper.mapToDtos(foundEntities);
    }
}
