package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMileageMapper;
import by.bsu.wialontransport.crud.repository.TrackerMileageRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackerMileageService
        extends AbstractCRUDService<Long, TrackerMileageEntity, TrackerMileage, TrackerMileageMapper, TrackerMileageRepository> {

    public TrackerMileageService(final TrackerMileageMapper mapper, final TrackerMileageRepository repository) {
        super(mapper, repository);
    }

}
