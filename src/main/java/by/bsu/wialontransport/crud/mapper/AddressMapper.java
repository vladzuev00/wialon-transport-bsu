package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper extends Mapper<AddressEntity, Address> {

    public AddressMapper(final ModelMapper modelMapper) {
        super(modelMapper, AddressEntity.class, Address.class);
    }

    @Override
    protected Address createDto(final AddressEntity source) {
        return new Address(
                source.getId(),
                source.getBoundingBox(),
                source.getCenter(),
                source.getCityName(),
                source.getCountryName(),
                source.getGeometry()
        );
    }

    @Override
    protected void mapSpecificFields(final Address source, final AddressEntity destination) {

    }
}
