package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.mapper.TrackerOdometerMapper;
import by.bsu.wialontransport.crud.repository.TrackerMileageRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackerOdometerService
        extends AbstractCRUDService<Long, TrackerMileageEntity, TrackerMileage, TrackerOdometerMapper, TrackerMileageRepository> {

    public TrackerOdometerService(final TrackerOdometerMapper mapper, final TrackerMileageRepository repository) {
        super(mapper, repository);
    }

}
