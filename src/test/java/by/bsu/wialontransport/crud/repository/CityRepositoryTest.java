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

import static by.bsu.wialontransport.util.EntityTestUtil.checkEquals;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityRepositoryTest extends AbstractContextTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void cityShouldBeFoundById() {
        super.startQueryCount();
        final CityEntity actual = this.repository.findById(257L).orElseThrow();
        super.checkQueryCount(1);

        final CityEntity expected = CityEntity.builder()
                .id(257L)
                .address(super.entityManager.getReference(AddressEntity.class, 255L))
                .searchingCitiesProcess(super.entityManager.getReference(SearchingCitiesProcessEntity.class, 256L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder()
                .address(super.entityManager.getReference(AddressEntity.class, 255L))
                .searchingCitiesProcess(super.entityManager.getReference(SearchingCitiesProcessEntity.class, 256L))
                .build();

        super.startQueryCount();
        this.repository.save(givenCity);
        super.checkQueryCount(2);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        assertFalse(exists);
    }
}
