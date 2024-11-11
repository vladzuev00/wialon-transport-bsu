package by.bsu.wialontransport.util;

import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.model.TempTrack;
import com.opencsv.CSVReader;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.util.List;

import static java.lang.Double.parseDouble;

@UtilityClass
public final class CsvReadingTestUtil {
    private static final CoordinateFactory COORDINATE_FACTORY = new CoordinateFactory();

    public static TempTrack readTrack(final String filePath)
            throws Exception {
//        try (final CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
//            final List<RequestCoordinate> coordinates = readCoordinates(csvReader);
//            return new TempTrack(coordinates);
//        }
        return null;
    }

    public static List<GpsCoordinate> readCoordinates(final String filePath)
            throws Exception {
        try (final CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll()
                    .stream()
                    .map(COORDINATE_FACTORY::create)
                    .toList();
        }
    }

    private static final class CoordinateFactory {
        private static final int INDEX_READ_PROPERTY_LATITUDE = 0;
        private static final int INDEX_READ_PROPERTY_LONGITUDE = 1;

        public GpsCoordinate create(final String[] readProperties) {
            final double latitude = parseDouble(readProperties[INDEX_READ_PROPERTY_LATITUDE]);
            final double longitude = parseDouble(readProperties[INDEX_READ_PROPERTY_LONGITUDE]);
            return new GpsCoordinate(latitude, longitude);
        }
    }
}
