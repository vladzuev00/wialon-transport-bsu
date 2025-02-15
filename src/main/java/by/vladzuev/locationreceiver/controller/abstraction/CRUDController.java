package by.vladzuev.locationreceiver.controller.abstraction;

import by.vladzuev.locationreceiver.controller.exception.NoSuchEntityException;
import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
public abstract class CRUDController<
        ID,
        DTO extends Dto<ID>,
        SERVICE extends CRUDService<ID, ?, DTO, ?, ?>,
        RESPONSE_VIEW extends View<ID>,
        SAVE_VIEW extends View<ID>,
        UPDATE_VIEW extends View<ID>
        > {
    private final SERVICE service;

    @GetMapping("/{id}")
    public ResponseEntity<RESPONSE_VIEW> findById(@PathVariable final ID id) {
        return findUnique(service -> service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RESPONSE_VIEW> save(@Valid @RequestBody final SAVE_VIEW view) {
        return execute(() -> createDtoBySaveView(view), (service, dto) -> service.save(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<RESPONSE_VIEW>> saveAll(@RequestBody final List<@Valid SAVE_VIEW> views) {
        final List<DTO> dtos = mapToDtos(views);
        final List<DTO> savedDtos = service.saveAll(dtos);
        final List<RESPONSE_VIEW> responseViews = createResponseViews(savedDtos);
        return ok(responseViews);
    }

    @PutMapping
    public ResponseEntity<RESPONSE_VIEW> update(@Valid @RequestBody final UPDATE_VIEW view) {
        return execute(() -> createDtoByUpdateView(view), (service, dto) -> service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final ID id) {
        service.delete(id);
        return noContent().build();
    }

    protected abstract DTO createDtoBySaveView(final SAVE_VIEW view);

    protected abstract DTO createDtoByUpdateView(final UPDATE_VIEW view);

    protected abstract RESPONSE_VIEW createResponseView(final DTO dto);

    protected ResponseEntity<RESPONSE_VIEW> findUnique(final Function<SERVICE, Optional<DTO>> operation) {
        return operation.apply(service)
                .map(this::createResponseView)
                .map(ResponseEntity::ok)
                .orElseThrow(CRUDController::createNoSuchEntityException);
    }

    //TODO: test
    protected static <I, D extends Dto<I>, S extends CRUDService<I, ?, D, ?, ?>> D findRelation(final S service,
                                                                                                final I id) {
        return service.findById(id).orElseThrow(CRUDController::createNoSuchEntityException);
    }

    private static NoSuchEntityException createNoSuchEntityException() {
        return new NoSuchEntityException("Entity wasn't found");
    }

    private ResponseEntity<RESPONSE_VIEW> execute(final Supplier<DTO> argumentSupplier,
                                                  final BiFunction<SERVICE, DTO, DTO> operation) {
        final DTO dto = argumentSupplier.get();
        final DTO resultDto = operation.apply(service, dto);
        final RESPONSE_VIEW responseView = createResponseView(resultDto);
        return ok(responseView);
    }

    private List<DTO> mapToDtos(final List<SAVE_VIEW> views) {
        return views.stream()
                .map(this::createDtoBySaveView)
                .toList();
    }

    private List<RESPONSE_VIEW> createResponseViews(final List<DTO> dtos) {
        return dtos.stream()
                .map(this::createResponseView)
                .toList();
    }
}
