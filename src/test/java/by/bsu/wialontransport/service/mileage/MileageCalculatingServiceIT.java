package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readTrack;
import static org.junit.Assert.assertEquals;

public final class MileageCalculatingServiceIT extends AbstractContextTest {

    private static final String SLASH = "/";

    private static final String FILE_NAME_WITH_FIRST_TRACK_POINTS = "track_460_40000.csv";
    private static final String FILE_PATH_WITH_FIRST_TRACK_POINTS = FOLDER_PATH_WITH_TRACK_POINTS
            + SLASH + FILE_NAME_WITH_FIRST_TRACK_POINTS;

    private static final String FILE_NAME_WITH_SECOND_TRACK_POINTS = "track_460_64000.csv";
    private static final String FILE_NAME_WITH_THIRD_TRACK_POINTS = "track_460_131000.csv";
    private static final String FILE_NAME_WITH_FOURTH_TRACK_POINTS = "unit_460_13000.csv";

    @Autowired
    private MileageCalculatingService mileageCalculatingService;

    @Test
    @Sql("classpath:sql/insert-belarus-cities.sql")
    public void mileageShouldBeCalculatedForFirstTrackPoints()
            throws Exception {
        final Track givenTrack = readTrack(FILE_PATH_WITH_FIRST_TRACK_POINTS);

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1262.4029441634757, 3861.696139540489);
        assertEquals(expected, actual);
    }

}
