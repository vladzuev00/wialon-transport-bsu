package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.TrackerOdometer;
import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import by.bsu.wialontransport.crud.mapper.TrackerOdometerMapper;
import by.bsu.wialontransport.crud.repository.TrackerOdometerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackerOdometerService
        extends AbstractCRUDService<Long, TrackerOdometerEntity, TrackerOdometer, TrackerOdometerMapper, TrackerOdometerRepository> {

    public TrackerOdometerService(final TrackerOdometerMapper mapper, final TrackerOdometerRepository repository) {
        super(mapper, repository);
    }

}
