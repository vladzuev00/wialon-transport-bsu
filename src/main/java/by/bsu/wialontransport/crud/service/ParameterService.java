package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.mapper.ParameterMapper;
import by.bsu.wialontransport.crud.repository.ParameterRepository;
import org.springframework.stereotype.Service;

@Service
public class ParameterService
        extends AbstractCRUDService<Long, ParameterEntity, Parameter, ParameterMapper, ParameterRepository> {

    public ParameterService(final ParameterMapper mapper, final ParameterRepository repository) {
        super(mapper, repository);
    }

    @Override
    protected void configureBeforeSave(final ParameterEntity entity) {

    }
}
