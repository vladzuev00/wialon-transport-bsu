package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.TempTrack;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.model.TempTrack.create;
import static by.bsu.wialontransport.util.CsvReadingTestUtil.readTrack;
import static java.io.File.separator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class RamerDouglasPeuckerTrackSimplifierTest extends AbstractContextTest {
    private static final String FOLDER_PATH_WITH_TRACK_POINTS = "./src/test/resources/tracksimplifying/csv";

    private static final String FILE_NAME_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED = "track-points-to-be-simplified.csv";
    private static final String FILE_PATH_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED
            = FOLDER_PATH_WITH_TRACK_POINTS + separator + FILE_NAME_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED;

    private static final String FILE_NAME_WITH_TRACK_POINTS_AFTER_SIMPLIFYING = "track-points-after-simplifying.csv";
    private static final String FILE_PATH_WITH_TRACK_POINTS_AFTER_SIMPLIFIED
            = FOLDER_PATH_WITH_TRACK_POINTS + separator + FILE_NAME_WITH_TRACK_POINTS_AFTER_SIMPLIFYING;

    @Autowired
    private RamerDouglasPeuckerCoordinatesSimplifier trackSimplifier;

    @Test
    public void trackShouldBeSimplified() {
        final TempTrack givenTrack = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(2., 3.),
                new RequestCoordinate(5., 1.),
                new RequestCoordinate(6., 4.),
                new RequestCoordinate(9., 6.),
                new RequestCoordinate(11., 4.),
                new RequestCoordinate(13., 3.),
                new RequestCoordinate(14., 2.),
                new RequestCoordinate(18., 5.)
        );

        final TempTrack actual = this.trackSimplifier.simplify(givenTrack);
        final TempTrack expected = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(5., 1.),
                new RequestCoordinate(6., 4.),
                new RequestCoordinate(9., 6.),
                new RequestCoordinate(14., 2.),
                new RequestCoordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackWithSamePointsShouldBeSimplified() {
        final TempTrack givenTrack = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(2., 3.),
                new RequestCoordinate(2., 3.),
                new RequestCoordinate(5., 1.),
                new RequestCoordinate(6., 4.),
                new RequestCoordinate(9., 6.),
                new RequestCoordinate(11., 4.),
                new RequestCoordinate(13., 3.),
                new RequestCoordinate(14., 2.),
                new RequestCoordinate(18., 5.),
                new RequestCoordinate(18., 5.)
        );

        final TempTrack actual = this.trackSimplifier.simplify(givenTrack);
        final TempTrack expected = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(5., 1.),
                new RequestCoordinate(6., 4.),
                new RequestCoordinate(9., 6.),
                new RequestCoordinate(14., 2.),
                new RequestCoordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackWithoutPointsShouldNotBeSimplified() {
        final TempTrack givenTrack = create();

        final TempTrack actual = this.trackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void trackWithTwoPointsShouldNotBeSimplified() {
        final TempTrack givenTrack = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(5., 1.)
        );

        final TempTrack actual = this.trackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void trackShouldNotBeSimplifiedBecauseOfEpsilonIsNotPositive() {
        final CoordinatesSimplifier givenTrackSimplifier = new RamerDouglasPeuckerCoordinatesSimplifier(0.);
        final TempTrack givenTrack = create(
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(1., 5.),
                new RequestCoordinate(2., 3.),
                new RequestCoordinate(2., 3.),
                new RequestCoordinate(5., 1.),
                new RequestCoordinate(6., 4.),
                new RequestCoordinate(9., 6.),
                new RequestCoordinate(11., 4.),
                new RequestCoordinate(13., 3.),
                new RequestCoordinate(14., 2.),
                new RequestCoordinate(18., 5.),
                new RequestCoordinate(18., 5.)
        );

        final TempTrack actual = givenTrackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void bigTrackShouldBeSimplified()
            throws Exception {
        final TempTrack givenTrack = readTrack(FILE_PATH_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED);

        final TempTrack actual = this.trackSimplifier.simplify(givenTrack);
        final TempTrack expected = readTrack(FILE_PATH_WITH_TRACK_POINTS_AFTER_SIMPLIFIED);
        assertEquals(expected, actual);
    }

}
