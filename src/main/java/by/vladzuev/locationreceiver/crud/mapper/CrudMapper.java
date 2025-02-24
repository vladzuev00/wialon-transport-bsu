package by.vladzuev.locationreceiver.crud.mapper;

import java.util.List;

public interface CrudMapper<DTO, ENTITY> {
    DTO mapEntity(final ENTITY entity);

    ENTITY mapDto(final DTO dto);

    List<DTO> mapEntities(final Iterable<ENTITY> entities);

    List<ENTITY> mapDtos(final Iterable<DTO> dtos);
}
