package by.bsu.wialontransport.service.mileageaccumulating;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerMileageService;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.service.mileage.MileageCalculatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class MileageAccumulatingService {
    private final DataService dataService;
    private final MileageCalculatingService mileageCalculatingService;
    private final TrackerMileageService trackerMileageService;

    public void accumulate(final Data inboundData) {
        final Tracker tracker = inboundData.getTracker();
        this.dataService.findTrackerLastData(tracker)
                .map(previousData -> this.calculateMileage(previousData, inboundData))
                .ifPresent(mileage -> this.trackerMileageService.increaseMileage(tracker, mileage));
    }

//    public void accumulate(final List<Data> inboundData) {
//        final Tracker tracker = inboundData.getTracker();
//        this.dataService.findTrackerLastData(tracker)
//    }

    private Mileage calculateMileage(final Data previousData, final Data inboundData) {
//        final Coordinate previousCoordinate = previousData.findCoordinate();
//        final Coordinate inboundCoordinate = inboundData.findCoordinate();
//        return this.mileageCalculatingService.calculate(previousCoordinate, inboundCoordinate);
        return null;
    }
}
