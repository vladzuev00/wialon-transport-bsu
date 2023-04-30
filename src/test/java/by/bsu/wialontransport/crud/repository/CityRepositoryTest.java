package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;

import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.util.EntityUtil.checkEquals;
import static by.bsu.wialontransport.util.GeometryUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;

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
    @Sql(statements = "INSERT INTO cities(id, searching_cities_process_id) VALUES(255, 256)")
    public void cityShouldBeFoundById() {
        super.startQueryCount();
        final CityEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final CityEntity expected = CityEntity.cityBuilder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
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
        final CityEntity givenCity = CityEntity.cityBuilder()
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .searchingCitiesProcess(super.entityManager.getReference(SearchingCitiesProcessEntity.class, 256L))
                .build();

        super.startQueryCount();
        this.repository.save(givenCity);
        super.checkQueryCount(3);
    }
}
