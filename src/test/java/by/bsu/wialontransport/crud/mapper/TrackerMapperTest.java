package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.util.entity.TrackerEntityUtil.checkEquals;
import static org.hibernate.Hibernate.isInitialized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

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
                .user(createUserDto(256L))
                .mileage(createTrackerMileageDto(257L))
                .build();

        final TrackerEntity actual = mapper.mapToEntity(givenDto);
        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(createUserEntity(256L))
                .mileage(createTrackerMileageEntity(257L))
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
                .user(createUserEntity(256L))
                .mileage(createTrackerMileageEntity(257L))
                .build();

        final Tracker actual = mapper.mapToDto(givenEntity);
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(createUserDto(256L))
                .mileage(createTrackerMileageDto(257L))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
            final UserEntity givenUser = createUserEntity(256L);
            final TrackerMileageEntity givenMileage = createTrackerMileageEntity(257L);
            final TrackerEntity givenEntity = TrackerEntity.builder()
                    .id(255L)
                    .imei("11112222333344445555")
                    .password("password")
                    .phoneNumber("447336934")
                    .user(givenUser)
                    .mileage(givenMileage)
                    .build();

            mockedStatic
                    .when(() -> isInitialized(same(givenUser)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenMileage)))
                    .thenReturn(false);

            final Tracker actual = mapper.mapToDto(givenEntity);
            final Tracker expected = Tracker.builder()
                    .id(255L)
                    .imei("11112222333344445555")
                    .password("password")
                    .phoneNumber("447336934")
                    .build();
            assertEquals(expected, actual);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static UserEntity createUserEntity(final Long id) {
        return UserEntity.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static User createUserDto(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerMileageEntity createTrackerMileageEntity(final Long id) {
        return TrackerMileageEntity.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerMileage createTrackerMileageDto(final Long id) {
        return TrackerMileage.builder()
                .id(id)
                .build();
    }
}
