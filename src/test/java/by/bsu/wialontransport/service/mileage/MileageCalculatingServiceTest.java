package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.model.Track.create;
import static org.junit.Assert.assertEquals;

//TODO: для каждого case-а нарисовать картинку
public final class MileageCalculatingServiceTest extends AbstractContextTest {
    @Autowired
    private MileageCalculatingService mileageCalculatingService;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case1() {
        final Track givenTrack = create(
                new Coordinate(1.015, 2.025),
                new Coordinate(1.025, 2.025)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0, 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case2() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.015, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.1131947333998116, 0);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case3() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0, 2.226389466799623);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case4() {
        final Track givenTrack = create(
                new Coordinate(1.015, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0, 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case5() {
        final Track givenTrack = create(
                new Coordinate(1.013, 2.015),
                new Coordinate(1.016, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0.3339584200199558, 0.);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case6() {
        final Track givenTrack = create(
                new Coordinate(1.02, 2.015),
                new Coordinate(1.03, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0., 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case7() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.01, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0, 0.5565973666999181);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case8() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.015, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.1131947333998116, 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case9() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.015, 2.018),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.1621945137415006, 1.1621942170972457);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case10() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.013, 2.015),
                new Coordinate(1.017, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.3358336800797739, 0.8905557867198493);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case11() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.013, 2.015),
                new Coordinate(1.017, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.3358336800797739, 0.8905557867198493);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case12() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.013, 2.013),
                new Coordinate(1.017, 2.017),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.547624155016635, 0.9179553209649113);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(258, ST_GeomFromText('POLYGON((2 1.005, 2 1.025, 2.03 1.025, 2.03 1.005, 2 1.005))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.005, 2 1.01, 2.02 1.025, 2.03 1.02, 2.01 1.005))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(259, 258, 256)")
    public void case13() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.015, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.1131947333998116, 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(258, ST_GeomFromText('POLYGON((2 1.005, 2 1.025, 2.03 1.025, 2.03 1.005, 2 1.005))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.005, 2 1.01, 2.02 1.025, 2.03 1.02, 2.01 1.005))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(259, 258, 256)")
    public void case14() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.012, 2.018),
                new Coordinate(1.015, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(1.3200150590669757, 1.1131947333998116);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.02 1.01, 2.01 1.01))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((2.01 1.01, 2.01 1.02, 2.02 1.02, 2.01 1.01))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 1000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void case15() {
        final Track givenTrack = create(
                new Coordinate(1.005, 2.015),
                new Coordinate(1.015, 2.015),
                new Coordinate(1.025, 2.015)
        );

        final Mileage actual = this.mileageCalculatingService.calculate(givenTrack);
        final Mileage expected = new Mileage(0, 2.226389466799623);
        assertEquals(expected, actual);
    }
}
