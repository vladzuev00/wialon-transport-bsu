package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.TempTrack;

@FunctionalInterface
public interface TrackSimplifier {
    TempTrack simplify(final TempTrack track);
}
