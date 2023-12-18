package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMileageMapper;
import by.bsu.wialontransport.crud.repository.TrackerMileageRepository;
import by.bsu.wialontransport.model.Mileage;
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
        return super.findInt(
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
