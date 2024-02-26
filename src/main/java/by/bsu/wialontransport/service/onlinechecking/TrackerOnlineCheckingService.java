package by.bsu.wialontransport.service.onlinechecking;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.model.online.OnlineStatus;
import by.bsu.wialontransport.model.online.TrackerOnlineInfo;
import by.bsu.wialontransport.model.online.TrackerOnlineInfo.LastData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static by.bsu.wialontransport.model.online.OnlineStatus.ONLINE;
import static by.bsu.wialontransport.model.online.TrackerOnlineInfo.createNoDataInfo;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public final class TrackerOnlineCheckingService {
    private final DataService dataService;
    private final long lastDataSecondThreshold;

    public TrackerOnlineCheckingService(final DataService dataService,
                                        @Value("${tracker-online.last-data-second-threshold}") final long lastDataSecondThreshold) {
        this.dataService = dataService;
        this.lastDataSecondThreshold = lastDataSecondThreshold;
    }

    public TrackerOnlineInfo check(final Tracker tracker) {
        return dataService.findTrackerLastData(tracker)
                .map(lastData -> createInfo(tracker, lastData))
                .orElseGet(() -> createNoDataInfo(tracker));
    }

    private TrackerOnlineInfo createInfo(final Tracker tracker, final Data lastData) {
        final OnlineStatus status = isThresholdNotExceeded(lastData) ? ONLINE : OFFLINE;
        final LastData lastDataProject = projectLastData(lastData);
        return new TrackerOnlineInfo(tracker, lastDataProject, status);
    }

    private boolean isThresholdNotExceeded(final Data data) {
        final long secondsSinceData = between(data.getDateTime(), now()).get(SECONDS);
        return secondsSinceData <= lastDataSecondThreshold;
    }

    private static LastData projectLastData(final Data data) {
        return new LastData(data.getDateTime(), data.getCoordinate());
    }
}
