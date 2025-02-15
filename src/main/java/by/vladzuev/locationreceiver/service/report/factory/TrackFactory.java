package by.vladzuev.locationreceiver.service.report.factory;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.model.CoordinateRequest;
import by.vladzuev.locationreceiver.model.TempTrack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TrackFactory {

    public TempTrack create(final List<Location> data) {
        final List<CoordinateRequest> coordinates = mapToCoordinates(data);
        return new TempTrack(coordinates);
    }

    private static List<CoordinateRequest> mapToCoordinates(final List<Location> data) {
//        return data.stream()
//                .map(Data::findCoordinate)
//                .toList();
        return null;
    }
}
