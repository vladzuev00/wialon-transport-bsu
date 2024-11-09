package by.bsu.wialontransport.service.onlinechecking;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.model.online.OnlineStatus;
import by.bsu.wialontransport.model.online.TrackerOnline;
import by.bsu.wialontransport.model.online.TrackerOnline.LastData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static by.bsu.wialontransport.model.online.OnlineStatus.ONLINE;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public final class TrackerOnlineService {
    private final LocationService dataService;
    private final long lastDataSecondThreshold;

    public TrackerOnlineService(final LocationService dataService,
                                @Value("${tracker-online.last-data-second-threshold}") final long lastDataSecondThreshold) {
        this.dataService = dataService;
        this.lastDataSecondThreshold = lastDataSecondThreshold;
    }

    public TrackerOnline check(final Long trackerId) {
        return dataService.findTrackerLastData(trackerId)
                .map(lastData -> createInfo(trackerId, lastData))
                .orElseGet(() -> createNoDataInfo(trackerId));
    }

    public TrackerOnline check(final Tracker tracker) {
        return check(tracker.getId());
    }

    private TrackerOnline createInfo(final Long trackerId, final Location lastData) {
        final OnlineStatus status = isThresholdNotExceeded(lastData) ? ONLINE : OFFLINE;
        final LastData lastDataProject = projectLastData(lastData);
        return new TrackerOnline(trackerId, lastDataProject, status);
    }

    private static TrackerOnline createNoDataInfo(final Long trackerId) {
        return TrackerOnline.builder()
                .trackerId(trackerId)
                .status(OFFLINE)
                .build();
    }

    private boolean isThresholdNotExceeded(final Location data) {
        final long secondsSinceData = between(data.getDateTime(), now()).get(SECONDS);
        return secondsSinceData <= lastDataSecondThreshold;
    }

    private static LastData projectLastData(final Location data) {
        return new LastData(data.getDateTime(), data.getCoordinate());
    }
}
