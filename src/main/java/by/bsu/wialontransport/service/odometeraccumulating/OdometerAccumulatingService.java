package by.bsu.wialontransport.service.odometeraccumulating;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerOdometerService;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.mileage.AbstractMileageCalculatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class OdometerAccumulatingService {
    private final DataService dataService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final TrackerOdometerService trackerOdometerService;

    public void accumulate(final Data inboundData) {
        final Long trackerId = inboundData.findTrackerId();
        final Optional<Data> optionalPreviousData = this.dataService.findTrackerLastDataByTrackerId(trackerId);
        optionalPreviousData.ifPresent(previousData -> );
    }

    private double calculateDistance()
}
