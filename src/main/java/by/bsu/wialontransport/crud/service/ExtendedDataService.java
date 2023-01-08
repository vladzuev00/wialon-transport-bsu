package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.crud.entity.ExtendedDataEntity;
import by.bsu.wialontransport.crud.mapper.ExtendedDataMapper;
import by.bsu.wialontransport.crud.repository.ExtendedDataRepository;
import org.springframework.stereotype.Service;

@Service
public class ExtendedDataService
        extends AbstractCRUDService<Long, ExtendedDataEntity, ExtendedData, ExtendedDataMapper, ExtendedDataRepository> {

    public ExtendedDataService(final ExtendedDataMapper mapper, final ExtendedDataRepository repository) {
        super(mapper, repository);
    }
}
