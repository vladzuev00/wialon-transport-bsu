package by.bsu.wialontransport.service.onlinechecking;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.model.online.OnlineStatus;
import by.bsu.wialontransport.model.online.TrackerOnline;
import by.bsu.wialontransport.model.online.TrackerOnline.LastData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static by.bsu.wialontransport.model.online.OnlineStatus.ONLINE;
import static by.bsu.wialontransport.model.online.TrackerOnline.createNoDataInfo;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public final class TrackerOnlineService {
    private final DataService dataService;
    private final long lastDataSecondThreshold;

    public TrackerOnlineService(final DataService dataService,
                                @Value("${tracker-online.last-data-second-threshold}") final long lastDataSecondThreshold) {
        this.dataService = dataService;
        this.lastDataSecondThreshold = lastDataSecondThreshold;
    }

    public TrackerOnline check(final Long trackerId) {
        return dataService.findTrackerLastData(trackerId)
                .
    }

    public TrackerOnline check(final Tracker tracker) {
        return dataService.findTrackerLastData(tracker)
                .map(lastData -> createInfo(tracker, lastData))
                .orElseGet(() -> createNoDataInfo(tracker));
    }

    private TrackerOnline createInfo(final Tracker tracker, final Data lastData) {
        final OnlineStatus status = isThresholdNotExceeded(lastData) ? ONLINE : OFFLINE;
        final LastData lastDataProject = projectLastData(lastData);
        return new TrackerOnline(tracker, lastDataProject, status);
    }

    private boolean isThresholdNotExceeded(final Data data) {
        final long secondsSinceData = between(data.getDateTime(), now()).get(SECONDS);
        return secondsSinceData <= lastDataSecondThreshold;
    }

    private static LastData projectLastData(final Data data) {
        return new LastData(data.getDateTime(), data.getCoordinate());
    }
}
