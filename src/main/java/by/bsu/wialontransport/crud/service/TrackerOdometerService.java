package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.mapper.TrackerOdometerMapper;
import by.bsu.wialontransport.crud.repository.TrackerOdometerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackerOdometerService
        extends AbstractCRUDService<Long, TrackerMileageEntity, TrackerMileage, TrackerOdometerMapper, TrackerOdometerRepository> {

    public TrackerOdometerService(final TrackerOdometerMapper mapper, final TrackerOdometerRepository repository) {
        super(mapper, repository);
    }

}
