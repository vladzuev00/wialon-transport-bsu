package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.TempTrack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TrackFactory {

    public TempTrack create(final List<Data> data) {
        final List<RequestCoordinate> coordinates = mapToCoordinates(data);
        return new TempTrack(coordinates);
    }

    private static List<RequestCoordinate> mapToCoordinates(final List<Data> data) {
//        return data.stream()
//                .map(Data::findCoordinate)
//                .toList();
        return null;
    }
}
