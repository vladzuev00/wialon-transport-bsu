package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

public final class TrackerMileageRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerMileageRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_mileages(id, urban, country) VALUES(255, 100.1, 200.2)")
    public void mileageShouldBeFoundById() {
        super.startQueryCount();
        final TrackerMileageEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(255L)
                .urban(100.1)
                .country(200.2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeInserted() {
        final TrackerMileageEntity givenMileage = TrackerMileageEntity.builder()
                .urban(100.1)
                .country(200.2)
                .build();

        super.startQueryCount();
        this.repository.save(givenMileage);
        super.checkQueryCount(1);
    }

    private static void checkEquals(final TrackerMileageEntity expected, final TrackerMileageEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }
}