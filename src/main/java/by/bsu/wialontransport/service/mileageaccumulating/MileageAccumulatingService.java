package by.bsu.wialontransport.service.mileageaccumulating;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerMileageService;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.mileage.AbstractMileageCalculatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class MileageAccumulatingService {
    private final DataService dataService;
    private final AbstractMileageCalculatingService mileageCalculatingService;
    private final TrackerMileageService trackerOdometerService;

    public void accumulate(final Data inboundData) {
        final Long trackerId = inboundData.findTrackerId();
        final Optional<Data> optionalPreviousData = this.dataService.findTrackerLastDataByTrackerId(trackerId);
        optionalPreviousData.ifPresent(previousData -> );
    }

    private Mileage calculateDistance()
}
