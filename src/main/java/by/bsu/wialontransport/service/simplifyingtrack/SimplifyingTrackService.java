package by.bsu.wialontransport.service.simplifyingtrack;

import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.simplifyingtrack.simplifier.TrackSimplifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class SimplifyingTrackService {
    private final TrackSimplifier trackSimplifier;

    public Track simplify(final Track track) {
        return this.trackSimplifier.simplify(track);
    }
}
