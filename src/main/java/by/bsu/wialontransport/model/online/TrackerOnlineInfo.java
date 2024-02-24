package by.bsu.wialontransport.model.online;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Coordinate;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static java.util.Optional.ofNullable;

@Value
public class TrackerOnlineInfo {
    Tracker tracker;
    LastData lastData;
    OnlineStatus status;

    public static TrackerOnlineInfo createNoDataInfo(final Tracker tracker) {
        return new TrackerOnlineInfo(tracker, null, OFFLINE);
    }

    public Optional<LastData> getLastData() {
        return ofNullable(lastData);
    }

    @Value
    public static class LastData {
        LocalDateTime dateTime;
        Coordinate coordinate;
    }
}
