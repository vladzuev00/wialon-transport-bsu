package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMapper;
import by.bsu.wialontransport.crud.repository.TrackerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrackerService
        extends AbstractCRUDService<Long, TrackerEntity, Tracker, TrackerMapper, TrackerRepository> {

    public TrackerService(final TrackerMapper mapper, final TrackerRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByImei(final String imei) {
        final Optional<TrackerEntity> optionalFoundEntity = super.repository.findByImei(imei);
        return optionalFoundEntity.map(super.mapper::mapToDto);
    }
}
