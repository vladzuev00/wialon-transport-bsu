package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class TrackerMapperTest extends AbstractContextTest {

    @Autowired
    private TrackerMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Tracker givenDto = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final TrackerEntity actual = this.mapper.mapToEntity(givenDto);
        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final TrackerEntity givenEntity = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .odometer(super.entityManager.getReference(TrackerMileageEntity.class, 0L))
                .build();

        final Tracker actual = this.mapper.mapToDto(givenEntity);
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getOdometer(), actual.getOdometer());
    }
}
