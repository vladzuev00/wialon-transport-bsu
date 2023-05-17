package by.bsu.wialontransport.controller.searchingcities;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.controller.searchingcities.mapper.SearchingCitiesProcessControllerMapper;
import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessPageResponse;
import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.bsu.wialontransport.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.bsu.wialontransport.controller.searchingcities.validator.StartSearchingCitiesRequestValidator;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/searchCities")
@RequiredArgsConstructor
@Validated
public class SearchingCitiesProcessController {
    private static final String MESSAGE_EXCEPTION_OF_NO_SUCH_PROCESS = "Process with id '%d' doesn't exist.";

    private final StartSearchingCitiesRequestValidator validator;
    private final StartingSearchingCitiesProcessService startingProcessService;
    private final SearchingCitiesProcessService processService;
    private final SearchingCitiesProcessControllerMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<SearchingCitiesProcessResponse> findById(@PathVariable final Long id) {
        final Optional<SearchingCitiesProcess> optionalFoundProcess = this.processService.findById(id);
        return optionalFoundProcess.map(this.mapper::mapToResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchEntityException(format(MESSAGE_EXCEPTION_OF_NO_SUCH_PROCESS, id)));
    }

    @GetMapping
    public ResponseEntity<SearchingCitiesProcessPageResponse> findByStatus(
            @RequestParam(name = "status") final Status status,
            @RequestParam(name = "pageNumber") @Min(0) final Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) final Integer pageSize) {
        final List<SearchingCitiesProcess> foundProcesses = this.processService.findByStatus(
                status, pageNumber, pageSize
        );
        return ok(this.mapper.mapToResponse(pageNumber, pageSize, foundProcesses));
    }

    @PostMapping
    public ResponseEntity<SearchingCitiesProcessResponse> start(
            @Valid @RequestBody final StartSearchingCitiesRequest request) {
        this.validator.validate(request);
        final SearchingCitiesProcess createdProcess = this.startingProcessService.start(
                request.getAreaCoordinate(), request.getSearchStep()
        );
        return ok(this.mapper.mapToResponse(createdProcess));
    }
}
