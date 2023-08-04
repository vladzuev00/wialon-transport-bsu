package by.bsu.wialontransport.service.report.model;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Mileage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class TrackerMovement {
    Tracker tracker;
    List<Data> data;
    Mileage mileage;

    public int findPointCounts() {
        return this.data.size();
    }

    public String findTrackerImei() {
        return this.tracker.getImei();
    }
}
