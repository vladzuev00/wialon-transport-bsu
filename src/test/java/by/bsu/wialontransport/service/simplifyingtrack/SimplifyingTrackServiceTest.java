package by.bsu.wialontransport.service.simplifyingtrack;

import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.simplifyingtrack.simplifier.TrackSimplifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SimplifyingTrackServiceTest {

    @Mock
    private TrackSimplifier mockedTrackSimplifier;

    private SimplifyingTrackService service;

    @Before
    public void initializeService() {
        this.service = new SimplifyingTrackService(this.mockedTrackSimplifier);
    }

    @Test
    public void trackShouldBeSimplified() {
        final Track givenTrack = new Track(emptyList());

        final Track givenSimplifiedTrack = new Track(emptyList());
        when(this.mockedTrackSimplifier.simplify(givenTrack)).thenReturn(givenSimplifiedTrack);

        final Track actual = this.service.simplify(givenTrack);
        assertSame(givenSimplifiedTrack, actual);
    }

}