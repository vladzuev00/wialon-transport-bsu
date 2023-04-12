package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper extends AbstractMapper<AddressEntity, Address> {

    public AddressMapper(final ModelMapper modelMapper) {
        super(modelMapper, AddressEntity.class, Address.class);
    }

    @Override
    protected Address createDto(final AddressEntity entity) {
        return new Address(entity.getId(),
                entity.getBoundingBox(),
                entity.getCenter(),
                entity.getCityName(),
                entity.getCountryName()
        );
    }
}
