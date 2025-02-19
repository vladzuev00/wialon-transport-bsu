package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.crud.mapper.AddressMapper;
import by.vladzuev.locationreceiver.crud.repository.AddressRepository;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Service
public class AddressService extends CRUDService<Long, AddressEntity, Address, AddressMapper, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByPoint(final Point point) {
        return Optional.empty();
//        return findUniqueDto(
//                repository -> repository.findByGpsCoordinates(
//                        coordinate.getLatitude(),
//                        coordinate.getLongitude()
//                )
//        );
    }

    @Transactional(readOnly = true)
    public Optional<Address> findByGeometry(final Geometry geometry) {
        return Optional.empty();
//        return findUniqueDto(repository -> repository.findByGeometry(geometry));
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return false;
//        return findBoolean(repository -> repository.isExistByGeometry(geometry));
    }

    @Transactional(readOnly = true)
    public Set<PreparedGeometry> findCitiesPreparedGeometriesIntersectedByLineString(final LineString lineString) {
        return null;
//        return findEntityStreamAndCollect(
//                repository -> repository.findCityAddressesIntersectedByLineString(lineString),
//                mapping(AddressService::extractPreparedGeometry, toUnmodifiableSet())
//        );
    }

    @Override
    protected void configureBeforeSave(final AddressEntity source) {

    }

    private static PreparedGeometry extractPreparedGeometry(final AddressEntity entity) {
        return prepare(entity.getGeometry());
    }
}
