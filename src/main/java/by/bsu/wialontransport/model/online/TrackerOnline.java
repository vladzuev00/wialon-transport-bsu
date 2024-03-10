package by.bsu.wialontransport.model.online;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.bsu.wialontransport.model.online.OnlineStatus.OFFLINE;
import static java.util.Optional.ofNullable;

@Value
@AllArgsConstructor
@Builder
public class TrackerOnline {
    Tracker tracker;
    LastData lastData;
    OnlineStatus status;

    public static TrackerOnline createNoDataInfo(final Tracker tracker) {
        return TrackerOnline.builder()
                .tracker(tracker)
                .status(OFFLINE)
                .build();
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
