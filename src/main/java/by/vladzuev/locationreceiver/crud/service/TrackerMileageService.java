package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.Mileage;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.crud.mapper.TrackerMileageMapper;
import by.vladzuev.locationreceiver.crud.repository.MileageRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackerMileageService extends CRUDService<
        Long,
        MileageEntity,
        Mileage,
        TrackerMileageMapper,
        MileageRepository
        > {

    public TrackerMileageService(final TrackerMileageMapper mapper, final MileageRepository repository) {
        super(mapper, repository);
    }

    public int increaseMileage(final Tracker tracker, final by.vladzuev.locationreceiver.model.Mileage mileageDelta) {
        return findInt(
                repository -> repository.increaseMileage(
                        tracker.getId(),
                        mileageDelta.getUrban(),
                        mileageDelta.getCountry()
                )
        );
    }

    @Override
    protected void configureBeforeSave(final MileageEntity entity) {

    }
}
