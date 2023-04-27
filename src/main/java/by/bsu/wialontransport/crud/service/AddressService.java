package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.mapper.AddressMapper;
import by.bsu.wialontransport.crud.repository.AddressRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService extends AbstractCRUDService<Long, AddressEntity, Address, AddressMapper, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public List<Address> findByGpsCoordinates(final double latitude, final double longitude) {
        final List<AddressEntity> foundEntities = super.repository.findByGpsCoordinates(latitude, longitude);
        return super.mapper.mapToDto(foundEntities);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findAddressByBoundingBox(final Geometry boundingBox) {
        final Optional<AddressEntity> optionalEntity = super.repository.findAddressByBoundingBox(boundingBox);
        return optionalEntity.map(super.mapper::mapToDto);
    }
}
