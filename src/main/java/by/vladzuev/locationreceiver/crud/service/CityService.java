package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.City;
import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import by.vladzuev.locationreceiver.crud.mapper.temp.CityMapper;
import by.vladzuev.locationreceiver.crud.repository.CityRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService extends CRUDService<Long, CityEntity, City, CityMapper, CityRepository> {

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return false;
//        return findBoolean(repository -> repository.isExistByGeometry(geometry));
    }

    @Override
    protected void configureBeforeSave(final CityEntity entity) {

    }
}
