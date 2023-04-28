package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.mapper.AddressMapper;
import by.bsu.wialontransport.crud.repository.AddressRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Optional<Address> findAddressByGeometry(final Geometry geometry) {
        final Optional<AddressEntity> optionalEntity = super.repository.findAddressByGeometry(geometry);
        return optionalEntity.map(super.mapper::mapToDto);
    }
}
