package by.vladzuev.locationreceiver.service.report.model;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.model.Mileage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class TrackerMovement {
    Tracker tracker;
    List<Location> data;
    Mileage mileage;

    public int findPointCounts() {
        return this.data.size();
    }

    public String findTrackerImei() {
        return this.tracker.getImei();
    }
}
