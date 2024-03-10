package by.bsu.wialontransport.controller.trackeronline;

import by.bsu.wialontransport.model.online.TrackerOnline;
import by.bsu.wialontransport.service.onlinechecking.TrackerOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/trackerOnline")
@RequiredArgsConstructor
public class TrackerOnlineController {
    private final TrackerOnlineService onlineService;

    @GetMapping("/{trackerId}")
    public ResponseEntity<TrackerOnline> check(@PathVariable final Long trackerId) {
        final TrackerOnline online = onlineService.check(trackerId);
        return ok(online);
    }
}
