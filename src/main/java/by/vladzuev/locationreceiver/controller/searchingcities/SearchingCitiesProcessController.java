package by.vladzuev.locationreceiver.controller.searchingcities;

import by.vladzuev.locationreceiver.controller.exception.NoSuchEntityException;
import by.vladzuev.locationreceiver.controller.searchingcities.mapper.SearchingCitiesProcessControllerMapper;
import by.vladzuev.locationreceiver.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.vladzuev.locationreceiver.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.vladzuev.locationreceiver.controller.searchingcities.validator.StartSearchingCitiesRequestValidator;
import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity.Status;
import by.vladzuev.locationreceiver.crud.service.SearchingCitiesProcessService;
import by.vladzuev.locationreceiver.model.AreaCoordinate;
import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/searchCities")
@RequiredArgsConstructor
public class SearchingCitiesProcessController {
    private final StartSearchingCitiesRequestValidator requestValidator;
    private final SearchingCitiesProcessService processService;
    private final SearchingCitiesProcessControllerMapper mapper;
    private final StartingSearchingCitiesProcessService startingProcessService;

    @GetMapping("/{id}")
    public ResponseEntity<SearchingCitiesProcessResponse> findById(@PathVariable final Long id) {
        return processService.findById(id)
                .map(mapper::mapToResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchEntityException(format("Process with id '%d' doesn't exist.", id)));
    }

    @GetMapping
    public ResponseEntity<Page<SearchingCitiesProcessResponse>> findByStatus(
            @RequestParam(name = "status") final Status status,
            @RequestParam(name = "pageNumber") @Min(0) final Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) final Integer pageSize
    ) {
        final Page<SearchingCitiesProcessResponse> responses = processService.findByStatusOrderedById(
                status,
                PageRequest.of(pageNumber, pageSize)
        ).map(mapper::mapToResponse);
        return ok(responses);
    }

    @PostMapping
    public ResponseEntity<SearchingCitiesProcessResponse> start(
            @Valid @RequestBody final StartSearchingCitiesRequest request
    ) {
        requestValidator.validate(request);
        final AreaCoordinate areaCoordinate = mapper.mapToAreaCoordinate(request.getAreaCoordinate());
        final SearchingCitiesProcess process = startingProcessService.start(areaCoordinate, request.getSearchStep());
        final SearchingCitiesProcessResponse response = mapper.mapToResponse(process);
        return ok(response);
    }
}
