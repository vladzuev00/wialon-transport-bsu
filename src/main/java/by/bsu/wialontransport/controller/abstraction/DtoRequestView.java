package by.bsu.wialontransport.controller.abstraction;

import by.bsu.wialontransport.crud.dto.Dto;

public interface DtoRequestView<DTO extends Dto<?>> {
    DTO createDto();
}
