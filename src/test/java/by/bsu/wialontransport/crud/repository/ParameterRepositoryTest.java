package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

//public final class ParameterRepositoryTest extends AbstractContextTest {
//
//    @Autowired
//    private ParameterRepository repository;
//
//    @Test
//    @Sql(statements = "INSERT INTO tracker_last_data"
//            + "(id, date, time, "
//            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
//            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
//            + "speed, course, height, amount_of_satellites, tracker_id) "
//            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
//    @Sql(statements = "INSERT INTO tracker_last_extended_data"
//            + "(id, reduction_precision, inputs, outputs, analog_inputs, driver_key_code) "
//            + "VALUES(255, 0.1, 12, 13, ARRAY[0.2, 0.3, 0.4], 'driver key code')")
//    @Sql(statements = "INSERT INTO parameters(id, name, type, value, extended_data_id) "
//            + "VALUES(255, 'name', 'INTEGER', '44', 255)")
//    public void parameterShouldBeFoundById() {
//        super.startQueryCount();
//        final ParameterEntity actual = this.repository.findById(255L).orElseThrow();
//        super.checkQueryCount(1);
//
//        final ParameterEntity expected = ParameterEntity.builder()
//                .id(255L)
//                .name("name")
//                .type(INTEGER)
//                .value("44")
//                .extendedData(super.entityManager.getReference(ExtendedDataEntity.class, 255L))
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    @Sql(statements = "INSERT INTO tracker_last_data"
//            + "(id, date, time, "
//            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
//            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
//            + "speed, course, height, amount_of_satellites, tracker_id) "
//            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
//    @Sql(statements = "INSERT INTO tracker_last_extended_data"
//            + "(id, reduction_precision, inputs, outputs, analog_inputs, driver_key_code) "
//            + "VALUES(255, 0.1, 12, 13, ARRAY[0.2, 0.3, 0.4], 'driver key code')")
//    public void parameterShouldBeInserted() {
//        final ParameterEntity givenParameter = ParameterEntity.builder()
//                .name("name")
//                .type(INTEGER)
//                .value("44")
//                .extendedData(super.entityManager.getReference(ExtendedDataEntity.class, 255L))
//                .build();
//
//        super.startQueryCount();
//        this.repository.save(givenParameter);
//        super.checkQueryCount(1);
//    }
//
//    private static void checkEquals(final ParameterEntity expected, final ParameterEntity actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getName(), actual.getName());
//        assertSame(expected.getType(), actual.getType());
//        assertEquals(expected.getValue(), actual.getValue());
//        assertEquals(expected.getExtendedData(), actual.getExtendedData());
//    }
//}
