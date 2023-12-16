package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.mapper.CityMapper;
import by.bsu.wialontransport.crud.repository.CityRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService extends AbstractCRUDService<Long, CityEntity, City, CityMapper, CityRepository> {

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return super.findBoolean(repository -> repository.isExistByGeometry(geometry));
    }

    @Override
    protected void configureBeforeSave(final CityEntity entity) {

    }
}
