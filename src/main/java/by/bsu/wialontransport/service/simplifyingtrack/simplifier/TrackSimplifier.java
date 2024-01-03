package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.TempTrack;
import by.bsu.wialontransport.model.Track;

public interface TrackSimplifier {
    Track simplify(final Track track);

    //TODO: remove
    TempTrack simplify(final TempTrack track);
}
