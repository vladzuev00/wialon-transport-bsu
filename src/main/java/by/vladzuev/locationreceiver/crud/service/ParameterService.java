package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.crud.mapper.ParameterMapper;
import by.vladzuev.locationreceiver.crud.repository.ParameterRepository;
import org.springframework.stereotype.Service;

@Service
public class ParameterService extends CRUDService<
        Long,
        ParameterEntity,
        Parameter,
        ParameterMapper,
        ParameterRepository
        > {

    public ParameterService(final ParameterMapper mapper, final ParameterRepository repository) {
        super(mapper, repository);
    }

    @Override
    protected void configureBeforeSave(final ParameterEntity entity) {

    }
}
