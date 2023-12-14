package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.entity.CityEntityUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityRepositoryTest extends AbstractContextTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldBeFoundById() {
        super.startQueryCount();
        final CityEntity actual = this.repository.findById(258L).orElseThrow();
        super.checkQueryCount(1);

        assertFalse(isAddressLoaded(actual));
        assertFalse(isSearchingCitiesProcessLoaded(actual));

        final CityEntity expected = CityEntity.builder()
                .id(258L)
                .address(super.entityManager.getReference(AddressEntity.class, 257L))
                .searchingCitiesProcess(super.entityManager.getReference(SearchingCitiesProcessEntity.class, 254L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder()
                .address(super.entityManager.getReference(AddressEntity.class, 255L))
                .searchingCitiesProcess(super.entityManager.getReference(SearchingCitiesProcessEntity.class, 254L))
                .build();

        super.startQueryCount();
        this.repository.save(givenCity);
        super.checkQueryCount(2);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                3, 1, 4, 1, 4, 2, 3, 2
        );

        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        assertFalse(exists);
    }
}
