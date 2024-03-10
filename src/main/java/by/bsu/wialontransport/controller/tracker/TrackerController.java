package by.bsu.wialontransport.controller.tracker;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO
@Validated
@RestController
@RequestMapping("/tracker")
@RequiredArgsConstructor
public class TrackerController {
    private final TrackerService trackerService;

//    @GetMapping("/user-trackers")
//    public ResponseEntity<List<Tracker>> findTrackersByUser(
//            @RequestParam(name = "pageNumber", defaultValue = "0") final int pageNumber,
//            @RequestParam(name = "pageSize", defaultValue = "5") final int pageSize,
//            @RequestParam(name = "trackerSortingKey", defaultValue = "IMEI") final TrackerSortingKey sortingKey
//    ) {
//
//    }

    @Value
    @AllArgsConstructor
    @Getter
    private static class TrackerView {
        Long id;
        String imei;
        String phoneNumber;
        TrackerMileage mileage;

        public TrackerView(final Tracker tracker) {
            id = tracker.getId();
            imei = tracker.getImei();
            phoneNumber = tracker.getPhoneNumber();
            mileage = tracker.getMileage();
        }
    }
}
