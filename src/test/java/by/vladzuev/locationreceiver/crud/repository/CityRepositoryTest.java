package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import by.vladzuev.locationreceiver.util.entity.CityEntityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.hibernate.Hibernate.isInitialized;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CityRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void cityShouldBeFoundById() {
        final Long givenId = 258L;

        final Optional<CityEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final CityEntity actual = optionalActual.get();
        assertFalse(isInitialized(actual.getAddress()));
        final CityEntity expected = new CityEntity(givenId, AddressEntity.builder().id(257L).build());
        CityEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder().address(AddressEntity.builder().id(255L).build()).build();

        final CityEntity actual = repository.save(givenCity);
        final CityEntity expected = new CityEntity(1L, AddressEntity.builder().id(255L).build());
        CityEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void intersectedByLineStringAddressesShouldBeStreamed() {
        final LineString givenLineString = geometryFactory.createLineString(
                new Coordinate[]{
                        new Coordinate(1.5, 1.5),
                        new Coordinate(3.5, 3.5),
                        new Coordinate(4.5, 4.5),
                        new Coordinate(1.5, 1.5)
                }
        );
        try (final Stream<AddressEntity> actual = repository.streamIntersectedAddresses(givenLineString)) {
            final Set<AddressEntity> actualAsSet = actual.collect(toUnmodifiableSet());
            final Set<AddressEntity> expectedAsSet = Set.of(AddressEntity.builder().id(257L).build());
            Assertions.assertEquals(expectedAsSet, actualAsSet);
        }
    }
}
