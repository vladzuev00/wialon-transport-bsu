package by.bsu.wialontransport.controller;

import by.bsu.wialontransport.crud.dto.Dto;

public interface RequestView<ID, DTO extends Dto<ID>> {
    DTO createDto();
}
