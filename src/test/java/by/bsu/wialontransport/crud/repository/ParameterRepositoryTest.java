package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ParameterRepositoryTest extends AbstractContextTest {

    @Autowired
    private ParameterRepository repository;

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    @Sql(statements = "INSERT INTO parameters(id, name, type, value, data_id) "
            + "VALUES(257, 'name', 'INTEGER', '44', 256)")
    public void parameterShouldBeFoundById() {
        super.startQueryCount();
        final ParameterEntity actual = this.repository.findById(257L).orElseThrow();
        super.checkQueryCount(1);

        final ParameterEntity expected = ParameterEntity.builder()
                .id(257L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255)")
    public void parameterShouldBeInserted() {
        final ParameterEntity givenParameter = ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();

        super.startQueryCount();
        this.repository.save(givenParameter);
        super.checkQueryCount(1);
    }

    private static void checkEquals(final ParameterEntity expected, final ParameterEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getData(), actual.getData());
    }
}
