package by.bsu.wialontransport.controller;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
public abstract class CRUDController<
        ID,
        DTO extends Dto<ID>,
        SERVICE extends CRUDService<ID, ?, DTO, ?, ?>,
        RESPONSE_VIEW,
        SAVED_VIEW extends RequestView<ID, DTO>,
        UPDATE_VIEW extends RequestView<ID, DTO>
        > {
    private final SERVICE service;

    @GetMapping("/{id}")
    public ResponseEntity<RESPONSE_VIEW> findById(@PathVariable final ID id) {
        return service.findById(id)
                .map(this::createResponseView)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchEntityException("Entity wasn't found"));
    }

    @PostMapping
    public ResponseEntity<RESPONSE_VIEW> save(@Valid @RequestBody final SAVED_VIEW view) {
        final DTO dto = view.createDto();
        final DTO savedDto = service.save(dto);
        final RESPONSE_VIEW responseView = createResponseView(savedDto);
        return ok(responseView);
    }

    @PostMapping
    public ResponseEntity<List<RESPONSE_VIEW>> saveAll(@RequestBody final List<@Valid SAVED_VIEW> views) {
        final List<DTO> dtos = mapToDtos(views);
        final List<DTO> savedDtos = service.saveAll(dtos);
        final List<RESPONSE_VIEW> responseViews = createResponseViews(savedDtos);
        return ok(responseViews);
    }

    @PutMapping
    public ResponseEntity<RESPONSE_VIEW> update(@Valid @RequestBody final UPDATE_VIEW view) {
        final
    }

    protected abstract RESPONSE_VIEW createResponseView(final DTO dto);

    private List<DTO> mapToDtos(final List<SAVED_VIEW> views) {
        return views.stream()
                .map(RequestView::createDto)
                .toList();
    }

    private List<RESPONSE_VIEW> createResponseViews(final List<DTO> dtos) {
        return dtos.stream()
                .map(this::createResponseView)
                .toList();
    }
}
