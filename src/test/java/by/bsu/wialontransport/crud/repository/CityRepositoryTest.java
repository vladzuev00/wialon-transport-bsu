package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.entity.CityEntityUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldBeFoundById() {
        final Long givenId = 258L;

        startQueryCount();
        final Optional<CityEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final CityEntity actual = optionalActual.get();
        assertFalse(isAddressFetched(actual));
        assertFalse(isSearchingCitiesProcessFetched(actual));

        final CityEntity expected = CityEntity.builder()
                .id(givenId)
                .address(entityManager.getReference(AddressEntity.class, 257L))
                .searchingCitiesProcess(entityManager.getReference(SearchingCitiesProcessEntity.class, 254L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder()
                .address(entityManager.getReference(AddressEntity.class, 255L))
                .searchingCitiesProcess(entityManager.getReference(SearchingCitiesProcessEntity.class, 254L))
                .build();

        startQueryCount();
        repository.save(givenCity);
        checkQueryCount(2);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                3, 1, 4, 1, 4, 2, 3, 2
        );

        final boolean exists = repository.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = repository.isExistByGeometry(givenGeometry);
        assertFalse(exists);
    }
}
