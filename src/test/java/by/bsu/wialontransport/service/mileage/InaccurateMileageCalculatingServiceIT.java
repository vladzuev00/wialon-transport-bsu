package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CsvReadingTestUtil.readTrack;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class InaccurateMileageCalculatingServiceIT extends AbstractContextTest {
    private static final String FOLDER_PATH_WITH_TRACK_POINTS = "./src/test/resources/tracks";
    private static final String SLASH = "/";

    private static final String FILE_NAME_WITH_FIRST_TRACK_POINTS = "track_460_40000.csv";
    private static final String FILE_PATH_WITH_FIRST_TRACK_POINTS = FOLDER_PATH_WITH_TRACK_POINTS
            + SLASH + FILE_NAME_WITH_FIRST_TRACK_POINTS;

    private static final String FILE_NAME_WITH_SECOND_TRACK_POINTS = "track_460_64000.csv";
    private static final String FILE_PATH_WITH_SECOND_TRACK_POINTS = FOLDER_PATH_WITH_TRACK_POINTS
            + SLASH + FILE_NAME_WITH_SECOND_TRACK_POINTS;

    private static final String FILE_NAME_WITH_THIRD_TRACK_POINTS = "track_460_131000.csv";
    private static final String FILE_PATH_WITH_THIRD_TRACK_POINTS = FOLDER_PATH_WITH_TRACK_POINTS
            + SLASH + FILE_NAME_WITH_THIRD_TRACK_POINTS;

    private static final String FILE_NAME_WITH_FOURTH_TRACK_POINTS = "unit_460_13000.csv";
    private static final String FILE_PATH_WITH_FOURTH_TRACK_POINTS = FOLDER_PATH_WITH_TRACK_POINTS
            + SLASH + FILE_NAME_WITH_FOURTH_TRACK_POINTS;

    @Autowired
    private InaccurateMileageCalculatingService mileageCalculatingService;

    @ParameterizedTest
    @Sql("classpath:sql/insert-belarus-cities.sql")
    @MethodSource("provideTrackPointsFilePathAndExpectedMileage")
    public void mileageShouldBeCalculatedForFirstTrackPoints(final String filePath, final Mileage expected)
            throws Exception {
        final Track givenTrack = readTrack(filePath);

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTrackPointsFilePathAndExpectedMileage() {
        return Stream.of(
                Arguments.of(
                        FILE_PATH_WITH_FIRST_TRACK_POINTS,
                        new Mileage(1262.4029441634757, 3861.696139540489)
                ),
                Arguments.of(
                        FILE_PATH_WITH_SECOND_TRACK_POINTS,
                        new Mileage(2013.8741502985813, 5485.861871017353)
                ),
                Arguments.of(
                        FILE_PATH_WITH_THIRD_TRACK_POINTS,
                        new Mileage(4451.411508875837, 10605.085916010763)
                ),
                Arguments.of(
                        FILE_PATH_WITH_FOURTH_TRACK_POINTS,
                        new Mileage(438.4721085487557, 1604.3095397903267)
                )
        );
    }

}
