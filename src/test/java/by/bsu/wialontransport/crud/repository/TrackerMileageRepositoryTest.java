package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;

public final class TrackerMileageRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerMileageRepository repository;

    @Test
    @Sql("classpath:sql/tracker-mileage/insert-tracker-mileages.sql")
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

    @Test
    public void mileageShouldBeIncreased() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.increaseMileage(255L, 50.5, 60.6);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final TrackerMileageEntity actual = this.repository.findById(1L).orElseThrow();
        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(1L)
                .urban(50.5)
                .country(60.6)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void mileageShouldNotBeIncreasedBecauseOfNotExistingTrackerId() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.increaseMileage(MAX_VALUE, 50.5, 60.6);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }
}
