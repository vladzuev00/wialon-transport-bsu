package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.mapper.AddressMapper;
import by.bsu.wialontransport.crud.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends AbstractCRUDService<Long, AddressEntity, Address, AddressMapper, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }

}
