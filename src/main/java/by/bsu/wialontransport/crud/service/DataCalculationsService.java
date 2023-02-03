package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import by.bsu.wialontransport.crud.mapper.DataCalculationsMapper;
import by.bsu.wialontransport.crud.repository.DataCalculationsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DataCalculationsService
        extends AbstractCRUDService<Long, DataCalculationsEntity, DataCalculations, DataCalculationsMapper, DataCalculationsRepository> {

    public DataCalculationsService(final DataCalculationsMapper mapper, final DataCalculationsRepository repository) {
        super(mapper, repository);
    }
}
