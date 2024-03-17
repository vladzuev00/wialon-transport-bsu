package by.bsu.wialontransport.controller.abstraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
public abstract class CRUDController<
        ID,
        DTO extends Dto<ID>,
        SERVICE extends CRUDService<ID, ?, DTO, ?, ?>,
        RESPONSE_VIEW,
        SAVE_VIEW extends DtoRequestView<DTO>,
        UPDATE_VIEW extends DtoRequestView<DTO>
        > {
    private final SERVICE service;

    @GetMapping("/{id}")
    public final ResponseEntity<RESPONSE_VIEW> findById(@PathVariable final ID id) {
        return findUnique(service -> service.findById(id));
    }

    @PostMapping
    public final ResponseEntity<RESPONSE_VIEW> save(@Valid @RequestBody final SAVE_VIEW view) {
        return execute(view, (service, dto) -> service.save(dto));
    }

    @PostMapping
    public final ResponseEntity<List<RESPONSE_VIEW>> saveAll(@RequestBody final List<@Valid SAVE_VIEW> views) {
        final List<DTO> dtos = mapToDtos(views);
        final List<DTO> savedDtos = service.saveAll(dtos);
        final List<RESPONSE_VIEW> responseViews = createResponseViews(savedDtos);
        return ok(responseViews);
    }

    @PutMapping
    public final ResponseEntity<RESPONSE_VIEW> update(@Valid @RequestBody final UPDATE_VIEW view) {
        return execute(view, (service, dto) -> service.update(dto));
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<?> delete(@PathVariable final ID id) {
        service.delete(id);
        return noContent().build();
    }

    protected abstract RESPONSE_VIEW createResponseView(final DTO dto);

    protected final ResponseEntity<RESPONSE_VIEW> findUnique(final Function<SERVICE, Optional<DTO>> operation) {
        return operation.apply(service)
                .map(this::createResponseView)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchEntityException("Entity wasn't found"));
    }

    private ResponseEntity<RESPONSE_VIEW> execute(final DtoRequestView<DTO> view,
                                                  final BiFunction<SERVICE, DTO, DTO> operation) {
        final DTO dto = view.createDto();
        final DTO resultDto = operation.apply(service, dto);
        final RESPONSE_VIEW responseView = createResponseView(resultDto);
        return ok(responseView);
    }

    private List<DTO> mapToDtos(final List<SAVE_VIEW> views) {
        return views.stream()
                .map(DtoRequestView::createDto)
                .toList();
    }

    private List<RESPONSE_VIEW> createResponseViews(final List<DTO> dtos) {
        return dtos.stream()
                .map(this::createResponseView)
                .toList();
    }
}
