package by.vladzuev.locationreceiver.service.mileage;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerMileageService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.Mileage;
import by.vladzuev.locationreceiver.model.Track;
import by.vladzuev.locationreceiver.service.mileage.calculator.MileageCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.vladzuev.locationreceiver.util.CollectionUtil.concat;

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
        return dataService.findLastFetchingParameters(tracker).map(Location::getCoordinate);
    }
}
