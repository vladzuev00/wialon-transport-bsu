package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerMileageService;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.mileage.calculator.MileageCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.util.CollectionUtil.concat;

@Service
@RequiredArgsConstructor
public final class MileageIncreasingService {
    private final LocationService dataService;
    private final MileageCalculator mileageCalculator;
    private final TrackerMileageService trackerMileageService;

    //TODO inject last data before(see in diplom doc)
    public void increase(final Track track) {
        final List<GpsCoordinate> pathCoordinates = findPathCoordinates(track);
        final Mileage mileageDelta = mileageCalculator.calculate(pathCoordinates);
        trackerMileageService.increaseMileage(track.getTracker(), mileageDelta);
    }

    private List<GpsCoordinate> findPathCoordinates(final Track track) {
        return findLastCoordinate(track.getTracker())
                .map(lastCoordinate -> concat(lastCoordinate, track.getCoordinates()))
                .orElse(track.getCoordinates());
    }

    private Optional<GpsCoordinate> findLastCoordinate(final Tracker tracker) {
        return dataService.findLastLocationFetchingParameters(tracker).map(Location::getCoordinate);
    }
}
