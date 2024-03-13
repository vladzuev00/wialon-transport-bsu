package by.bsu.wialontransport.controller;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class CRUDController<ID, DTO extends Dto<ID>, SERVICE extends CRUDService<ID, ?, DTO, ?, ?>, VIEW> {
    private final SERVICE service;

    @GetMapping("{id}")
    public final ResponseEntity<VIEW> findById(@PathVariable final ID id) {
//        return service.findById(id);
        return null;
    }

    protected VIEW findUniqueDto(final Function<SERVICE, Optional<DTO>> operation) {
//        return operation.apply(service)
//                .map(this::createView);
        return null;
    }

    protected abstract VIEW createView(final DTO dto);
}
