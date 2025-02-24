package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.util.entity.MileageEntityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MileageRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private MileageRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_mileages(id, urban, country) VALUES(255, 100.1, 200.2)")
    public void mileageShouldBeFoundById() {
        final Long givenId = 255L;

        final Optional<MileageEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final MileageEntity actual = optionalActual.get();
        final MileageEntity expected = new MileageEntity(255L, 100.1, 200.2);
        MileageEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeSaved() {
        final MileageEntity givenMileage = MileageEntity.builder().urban(100.1).country(200.2).build();

        final MileageEntity actual = repository.save(givenMileage);
        final MileageEntity expected = new MileageEntity(4L, 100.1, 200.2);
        MileageEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeIncreased() {
        final Long givenTrackerId = 255L;
        final double givenUrbanDelta = 50.5;
        final double givenCountryDelta = 60.6;

        final int actual = repository.increaseMileage(givenTrackerId, givenUrbanDelta, givenCountryDelta);
        final int expected = 1;
        Assertions.assertEquals(expected, actual);

        final MileageEntity actualMileage = repository.findById(1L).orElseThrow();
        final MileageEntity expectedMileage = new MileageEntity(1L, givenUrbanDelta, givenCountryDelta);
        MileageEntityUtil.assertEquals(expectedMileage, actualMileage);
    }

    @Test
    public void mileageShouldNotBeIncreasedBecauseOfNoSuchTracker() {
        final Long givenTrackerId = MAX_VALUE;
        final double givenUrbanDelta = 50.5;
        final double givenCountryDelta = 60.6;

        final int actual = repository.increaseMileage(givenTrackerId, givenUrbanDelta, givenCountryDelta);
        final int expected = 0;
        Assertions.assertEquals(expected, actual);
    }
}
