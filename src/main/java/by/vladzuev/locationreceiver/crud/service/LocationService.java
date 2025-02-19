package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import by.vladzuev.locationreceiver.crud.mapper.DataMapper;
import by.vladzuev.locationreceiver.crud.repository.LocationRepository;
import by.vladzuev.locationreceiver.model.DateInterval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LocationService extends CRUDService<Long, LocationEntity, Location, DataMapper, LocationRepository> {

    public LocationService(final DataMapper mapper, final LocationRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findLastFetchingParameters(final Tracker tracker) {
        return Optional.empty();
//        return findUniqueDto(
//                repository -> repository.findTrackerLastDataByTrackerIdFetchingParameters(
//                        tracker.getId()
//                )
//        );
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
        return Optional.empty();
//        return execute(repository -> repository.findTrackerLastDataDateTimeByTrackerId(tracker.getId()));
    }

    @Transactional(readOnly = true)
    public Optional<Location> findTrackerLastData(final Tracker tracker) {
        return findTrackerLastData(tracker.getId());
    }

    //TODO: test
    @Transactional(readOnly = true)
    public Optional<Location> findTrackerLastData(final Long trackerId) {
        return Optional.empty();
//        return findUniqueDto(repository -> repository.findTrackerLastDataByTrackerId(trackerId));
    }

    @Override
    protected void configureBeforeSave(final LocationEntity entity) {

    }
}
