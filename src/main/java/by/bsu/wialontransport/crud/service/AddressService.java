package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.mapper.AddressMapper;
import by.bsu.wialontransport.crud.repository.AddressRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.util.collection.CollectionUtil.mapList;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Service
public class AddressService
        extends AbstractCRUDService<Long, AddressEntity, Address, AddressMapper, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByGpsCoordinates(final double latitude, final double longitude) {
        final Optional<AddressEntity> optionalEntity = super.repository.findByGpsCoordinates(latitude, longitude);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByGeometry(final Geometry geometry) {
        final Optional<AddressEntity> optionalEntity = super.repository.findByGeometry(geometry);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return super.repository.isExistByGeometry(geometry);
    }

    //TODO: test
    @Transactional(readOnly = true)
    public List<PreparedGeometry> findCitiesPreparedGeometriesIntersectedByLineString(final LineString lineString) {
        final List<AddressEntity> foundEntities = super.repository.findCitiesAddressesIntersectedByLineString(
                lineString
        );
        return mapList(foundEntities, AddressService::extractPreparedGeometry);
    }

    private static PreparedGeometry extractPreparedGeometry(final AddressEntity entity) {
        final Geometry geometry = entity.getGeometry();
        return prepare(geometry);
    }
}
