package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TrackFactory {

    public Track create(final List<Data> data) {
        final List<Coordinate> coordinates = mapToCoordinates(data);
        return new Track(coordinates);
    }

    private static List<Coordinate> mapToCoordinates(final List<Data> data) {
//        return data.stream()
//                .map(Data::findCoordinate)
//                .toList();
        return null;
    }
}
