package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.TrackerMileage;
import by.vladzuev.locationreceiver.crud.entity.TrackerMileageEntity;
import by.vladzuev.locationreceiver.crud.mapper.TrackerMileageMapper;
import by.vladzuev.locationreceiver.crud.repository.TrackerMileageRepository;
import by.vladzuev.locationreceiver.model.Mileage;
import org.springframework.stereotype.Service;

@Service
public class TrackerMileageService extends CRUDService<
        Long,
        TrackerMileageEntity,
        TrackerMileage,
        TrackerMileageMapper,
        TrackerMileageRepository
        > {

    public TrackerMileageService(final TrackerMileageMapper mapper, final TrackerMileageRepository repository) {
        super(mapper, repository);
    }

    public int increaseMileage(final Tracker tracker, final Mileage mileageDelta) {
        return findInt(
                repository -> repository.increaseMileage(
                        tracker.getId(),
                        mileageDelta.getUrban(),
                        mileageDelta.getCountry()
                )
        );
    }

    @Override
    protected void configureBeforeSave(final TrackerMileageEntity entity) {

    }
}
