package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.util.entity.ParameterEntityUtil;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.vladzuev.locationreceiver.crud.enumeration.ParameterType.INTEGER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ParameterRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private ParameterRepository repository;

    @Test
    public void parameterShouldBeFoundById() {
        final Long givenId = 257L;

        final Optional<ParameterEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final ParameterEntity actual = optionalActual.get();
        assertFalse(Hibernate.isInitialized(actual.getLocation()));
        final ParameterEntity expected = new ParameterEntity(
                givenId,
                "name",
                INTEGER,
                "44",
                LocationEntity.builder().id(256L).build()
        );
        ParameterEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void parameterShouldBeSaved() {
        final ParameterEntity givenParameter = ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .location(LocationEntity.builder().id(256L).build())
                .build();

        final ParameterEntity actual = repository.save(givenParameter);
        final ParameterEntity expected = new ParameterEntity(
                1L,
                "name",
                INTEGER,
                "44",
                LocationEntity.builder().id(256L).build()
        );
        ParameterEntityUtil.assertEquals(expected, actual);
    }
}
