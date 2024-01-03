package by.bsu.wialontransport.service.simplifyingtrack;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.TempTrack;
import by.bsu.wialontransport.service.simplifyingtrack.simplifier.TrackSimplifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SimplifyingCoordinatesService {
    private final TrackSimplifier trackSimplifier;

    public List<Coordinate> simplify(final List<Coordinate> coordinates) {
        return null;
    }

    //TODO: remove
    public TempTrack simplify(final TempTrack track) {
        return this.trackSimplifier.simplify(track);
    }
}
