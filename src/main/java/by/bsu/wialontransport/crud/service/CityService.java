package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.mapper.CityMapper;
import by.bsu.wialontransport.crud.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService extends AbstractCRUDService<Long, CityEntity, City, CityMapper, CityRepository> {

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

}
