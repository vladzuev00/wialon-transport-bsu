//package by.bsu.wialontransport.service.simplifyingtrack;
//
//import by.bsu.wialontransport.model.TempTrack;
//import by.bsu.wialontransport.service.simplifyingtrack.simplifier.CoordinatesSimplifier;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static java.util.Collections.emptyList;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class SimplifyingTrackServiceTest {
//
//    @Mock
//    private CoordinatesSimplifier mockedTrackSimplifier;
//
//    private SimplifyingCoordinatesService service;
//
//    @Before
//    public void initializeService() {
//        this.service = new SimplifyingCoordinatesService(this.mockedTrackSimplifier);
//    }
//
//    @Test
//    public void trackShouldBeSimplified() {
//        final TempTrack givenTrack = new TempTrack(emptyList());
//
//        final TempTrack givenSimplifiedTrack = new TempTrack(emptyList());
//        when(this.mockedTrackSimplifier.simplify(givenTrack)).thenReturn(givenSimplifiedTrack);
//
//        final TempTrack actual = this.service.simplify(givenTrack);
//        assertSame(givenSimplifiedTrack, actual);
//    }
//
//}
