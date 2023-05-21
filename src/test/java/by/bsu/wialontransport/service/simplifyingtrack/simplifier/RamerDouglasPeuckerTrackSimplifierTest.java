package by.bsu.wialontransport.service.simplifyingtrack.simplifier;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import com.opencsv.CSVReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileReader;
import java.util.List;

import static by.bsu.wialontransport.model.Track.create;
import static java.io.File.separator;
import static java.lang.Double.parseDouble;
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

    private final CoordinateFactory coordinateFactory = new CoordinateFactory();

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

    //TODO: don't pass
    @Test
    public void bigTrackShouldBeSimplified()
            throws Exception {
        final TrackSimplifier givenTrackSimplifier = new RamerDouglasPeuckerTrackSimplifier(0.5);

        final Track givenTrack = readTrack(FILE_PATH_WITH_TRACK_POINTS_TO_BE_SIMPLIFIED);

        final Track actual = givenTrackSimplifier.simplify(givenTrack);
        final Track expected = readTrack(FILE_PATH_WITH_TRACK_POINTS_AFTER_SIMPLIFIED);
        assertEquals(expected, actual);
    }

    private Track readTrack(final String filePath)
            throws Exception {
        try (final CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            final List<Coordinate> coordinates = this.readCoordinates(csvReader);
            return new Track(coordinates);
        }
    }

    private List<Coordinate> readCoordinates(final CSVReader csvReader)
            throws Exception {
        return csvReader.readAll()
                .stream()
                .map(this.coordinateFactory::create)
                .toList();
    }

    private static final class CoordinateFactory {
        private static final int INDEX_READ_PROPERTY_LATITUDE = 0;
        private static final int INDEX_READ_PROPERTY_LONGITUDE = 1;

        public Coordinate create(final String[] readProperties) {
            final double latitude = parseDouble(readProperties[INDEX_READ_PROPERTY_LATITUDE]);
            final double longitude = parseDouble(readProperties[INDEX_READ_PROPERTY_LONGITUDE]);
            return new Coordinate(latitude, longitude);
        }

    }

}
