package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.mapper.DataMapper;
import by.bsu.wialontransport.crud.repository.DataRepository;
import by.bsu.wialontransport.model.DateInterval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DataService extends CRUDService<Long, DataEntity, Location, DataMapper, DataRepository> {

    public DataService(final DataMapper mapper, final DataRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findTrackerLastDataFetchingParameters(final Tracker tracker) {
        return findUniqueDto(
                repository -> repository.findTrackerLastDataByTrackerIdFetchingParameters(
                        tracker.getId()
                )
        );
    }

    @Transactional(readOnly = true)
    public Stream<Location> findDataByUserIdFetchingTrackerAndAddress(final User user, final DateInterval dateInterval) {
        return findDtoStream(
                repository -> repository.findDataByUserIdFetchingTrackerAndAddress(
                        user.getId(),
                        dateInterval.getStart(),
                        dateInterval.getEnd()
                )
        );
    }

    @Transactional(readOnly = true)
    public Optional<LocalDateTime> findTrackerLastDataDateTime(final Tracker tracker) {
        return execute(repository -> repository.findTrackerLastDataDateTimeByTrackerId(tracker.getId()));
    }

    @Transactional(readOnly = true)
    public Optional<Location> findTrackerLastData(final Tracker tracker) {
        return findTrackerLastData(tracker.getId());
    }

    //TODO: test
    @Transactional(readOnly = true)
    public Optional<Location> findTrackerLastData(final Long trackerId) {
        return findUniqueDto(repository -> repository.findTrackerLastDataByTrackerId(trackerId));
    }

    @Override
    protected void configureBeforeSave(final DataEntity entity) {

    }
}
