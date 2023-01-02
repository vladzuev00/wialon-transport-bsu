package by.bsu.wialontransport.crud.entity.converter;

import by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.findByValue;

@Converter
public final class LongitudeTypeConverter implements AttributeConverter<Type, Character> {

    @Override
    public Character convertToDatabaseColumn(final Type type) {
        return type.getValue();
    }

    @Override
    public Type convertToEntityAttribute(final Character typeValue) {
        return findByValue(typeValue);
    }
}
