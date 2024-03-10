package by.bsu.wialontransport.controller.trackeronline;

import by.bsu.wialontransport.model.online.TrackerOnline;
import by.bsu.wialontransport.service.onlinechecking.TrackerOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/tracker-online")
@RequiredArgsConstructor
public class TrackerOnlineController {
    private final TrackerOnlineService trackerOnlineService;

    @GetMapping
    public ResponseEntity<TrackerOnline> check(@RequestParam("trackerId") final Long trackerId) {
        final TrackerOnline trackerOnline = trackerOnlineService.check(trackerId);
        return ok(trackerOnline);
    }
}
