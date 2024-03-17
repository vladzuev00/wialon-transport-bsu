package by.bsu.wialontransport.controller.tracker;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.controller.tracker.view.SaveTrackerView;
import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.crud.service.TrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.lang.String.format;

//TODO
@Validated
@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class TrackerController {
    private final TrackerService trackerService;

    @GetMapping("/{id}")
    public ResponseEntity<TrackerView> findById(@PathVariable final Long id) {
//        return trackerService.findById(id)
//                .map(TrackerView::new)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> new NoSuchEntityException(format("Tracker with id '%d' doesn't exist.", id)));
        return null;
    }

    @PostMapping
    public ResponseEntity<TrackerView> save(@Valid @RequestBody final SaveTrackerView view) {
//        final Tracker savedTracker
        return null;
    }

//    @GetMapping("/user-trackers")
//    public ResponseEntity<List<Tracker>> findTrackersByUser(
//            @RequestParam(name = "pageNumber", defaultValue = "0") final int pageNumber,
//            @RequestParam(name = "pageSize", defaultValue = "5") final int pageSize,
//            @RequestParam(name = "trackerSortingKey", defaultValue = "IMEI") final TrackerSortingKey sortingKey
//    ) {
//
//    }
}
