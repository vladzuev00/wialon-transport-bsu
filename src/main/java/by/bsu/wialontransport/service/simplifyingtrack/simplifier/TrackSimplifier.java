package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.model.Track;

@FunctionalInterface
public interface TrackSimplifier {
    Track simplify(final Track track);
}
