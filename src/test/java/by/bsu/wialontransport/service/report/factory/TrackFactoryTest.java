//package by.bsu.wialontransport.service.report.factory;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.model.Track;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static by.bsu.wialontransport.util.CollectionUtil.mapToList;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public final class TrackFactoryTest extends AbstractContextTest {
//
//    @Autowired
//    private TrackFactory trackFactory;
//
//    @Test
//    public void trackShouldBeCreated() {
//        final List<Coordinate> givenCoordinates = List.of(
//                new Coordinate(5.5, 6.6),
//                new Coordinate(7.7, 8.8),
//                new Coordinate(9.9, 10.),
//                new Coordinate(11.1, 12.2)
//        );
//        final List<Data> givenData = createData(givenCoordinates);
//
//        final Track actual = this.trackFactory.create(givenData);
//        final Track expected = new Track(givenCoordinates);
//        assertEquals(expected, actual);
//    }
//
//    private static List<Data> createData(final List<Coordinate> coordinates) {
//        return mapToList(coordinates, TrackFactoryTest::createData);
//    }
//
//    private static Data createData(final Coordinate coordinate) {
//        final Data data = mock(Data.class);
//        when(data.findCoordinate()).thenReturn(coordinate);
//        return data;
//    }
//
//}
