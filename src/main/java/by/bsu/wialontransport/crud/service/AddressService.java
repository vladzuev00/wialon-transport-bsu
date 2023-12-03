package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.mapper.AddressMapper;
import by.bsu.wialontransport.crud.repository.AddressRepository;
import by.bsu.wialontransport.model.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Service
public class AddressService extends AbstractCRUDService<Long, AddressEntity, Address, AddressMapper, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByGpsCoordinates(final Coordinate coordinate) {
        final double latitude = coordinate.getLatitude();
        final double longitude = coordinate.getLongitude();
        return super.findUnique(repository -> repository.findByGpsCoordinates(latitude, longitude));
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByGeometry(final Geometry geometry) {
        return super.findUnique(repository -> repository.findByGeometry(geometry));
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return super.repository.isExistByGeometry(geometry);
    }

    @Transactional(readOnly = true)
    public List<PreparedGeometry> findCitiesPreparedGeometriesIntersectedByLineString(final LineString lineString) {
        final List<AddressEntity> foundEntities = super.repository.findCityAddressesIntersectedByLineString(
                lineString
        );
        return mapToList(foundEntities, AddressService::extractPreparedGeometry);
    }

    @Override
    protected AddressEntity configureBeforeSave(final AddressEntity source) {
        return source;
    }

    private static PreparedGeometry extractPreparedGeometry(final AddressEntity entity) {
        final Geometry geometry = entity.getGeometry();
        return prepare(geometry);
    }
}
