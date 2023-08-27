package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

public final class TrackerOdometerRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerOdometerRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_odometers(id, urban, country) VALUES(255, 100.1, 200.2)")
    public void odometerShouldBeFoundById() {
        super.startQueryCount();
        final TrackerOdometerEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final TrackerOdometerEntity expected = TrackerOdometerEntity.builder()
                .id(255L)
                .urban(100.1)
                .country(200.2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void odometerShouldBeInserted() {
        final TrackerOdometerEntity givenOdometer = TrackerOdometerEntity.builder()
                .urban(100.1)
                .country(200.2)
                .build();

        super.startQueryCount();
        this.repository.save(givenOdometer);
        super.checkQueryCount(1);
    }

    private static void checkEquals(final TrackerOdometerEntity expected, final TrackerOdometerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }
}
