package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.entity.AbstractEntity;

import java.util.List;

public interface CrudMapper<DTO extends Dto<?>, ENTITY extends AbstractEntity<?>> {
    DTO mapEntity(final ENTITY entity);

    ENTITY mapDto(final DTO dto);

    List<DTO> mapEntities(final Iterable<ENTITY> entities);

    List<ENTITY> mapDtos(final Iterable<DTO> dtos);
}
