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

import java.time.LocalDate;
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
        return super.findUnique(repository -> repository.findTrackerLastDataByTrackerId(trackerId));
    }

    @Transactional(readOnly = true)
    public List<Data> findDataWithTrackerAndAddress(final User user, final DateInterval dateInterval) {
        final Long userId = user.getId();
        final LocalDate startDateTime = dateInterval.getStart();
        final LocalDate endDateTime = dateInterval.getEnd();
        return super.find(
                repository -> repository.findDataWithTrackerAndAddressByUserId(userId, startDateTime, endDateTime)
        );
    }

    @Override
    protected void configureBeforeSave(final DataEntity entity) {

    }
}
