package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.model.Track.create;
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
    private RamerDouglasPeuckerTrackSimplifier trackSimplifier;

    @Test
    public void trackShouldBeSimplified() {
        final Track givenTrack = create(
                new Coordinate(1., 5.),
                new Coordinate(2., 3.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(11., 4.),
                new Coordinate(13., 3.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );

        final Track actual = this.trackSimplifier.simplify(givenTrack);
        final Track expected = create(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackWithSamePointsShouldBeSimplified() {
        final Track givenTrack = create(
                new Coordinate(1., 5.),
                new Coordinate(1., 5.),
                new Coordinate(2., 3.),
                new Coordinate(2., 3.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(11., 4.),
                new Coordinate(13., 3.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.),
                new Coordinate(18., 5.)
        );

        final Track actual = this.trackSimplifier.simplify(givenTrack);
        final Track expected = create(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackWithoutPointsShouldNotBeSimplified() {
        final Track givenTrack = create();

        final Track actual = this.trackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void trackWithTwoPointsShouldNotBeSimplified() {
        final Track givenTrack = create(
                new Coordinate(1., 5.),
                new Coordinate(5., 1.)
        );

        final Track actual = this.trackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void trackShouldNotBeSimplifiedBecauseOfEpsilonIsNotPositive() {
        final TrackSimplifier givenTrackSimplifier = new RamerDouglasPeuckerTrackSimplifier(0.);
        final Track givenTrack = create(
                new Coordinate(1., 5.),
                new Coordinate(1., 5.),
                new Coordinate(2., 3.),
                new Coordinate(2., 3.),
                new Coordinate(5., 1.),
                new Coordinate(6., 4.),
                new Coordinate(9., 6.),
                new Coordinate(11., 4.),
                new Coordinate(13., 3.),
                new Coordinate(14., 2.),
                new Coordinate(18., 5.),
                new Coordinate(18., 5.)
        );

        final Track actual = givenTrackSimplifier.simplify(givenTrack);
        assertSame(givenTrack, actual);
    }

    @Test
    public void bigTrackShouldBeSimplified()
            throws Exception {
        final Track givenTrack = readTrack(FILE_PATH_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED);

        final Track actual = this.trackSimplifier.simplify(givenTrack);
        final Track expected = readTrack(FILE_PATH_WITH_TRACK_POINTS_AFTER_SIMPLIFIED);
        assertEquals(expected, actual);
    }

}
