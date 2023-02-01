package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.mapper.DataMapper;
import by.bsu.wialontransport.crud.repository.DataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DataService extends AbstractCRUDService<Long, DataEntity, Data, DataMapper, DataRepository> {

    public DataService(final DataMapper mapper, final DataRepository repository) {
        super(mapper, repository);
    }
}
