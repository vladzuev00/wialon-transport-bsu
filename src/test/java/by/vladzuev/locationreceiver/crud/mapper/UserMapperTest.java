//package by.vladzuev.locationreceiver.crud.mapper;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.crud.dto.User;
//import by.vladzuev.locationreceiver.crud.entity.UserEntity;
//import by.vladzuev.locationreceiver.util.entity.UserEntityUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public final class UserMapperTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private UserMapper mapper;
//
//    @Test
//    public void dtoShouldBeMappedToEntity() {
//        final User givenDto = User.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(UserEntity.UserRole.USER)
//                .build();
//
//        final UserEntity actual = mapper.mapToEntity(givenDto);
//        final UserEntity expected = UserEntity.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(UserEntity.UserRole.USER)
//                .build();
//        assertNotNull(actual);
//        UserEntityUtil.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void entityShouldBeMappedToDto() {
//        final UserEntity givenEntity = UserEntity.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(UserEntity.UserRole.USER)
//                .build();
//
//        final User actual = mapper.createDto(givenEntity);
//        final User expected = User.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(UserEntity.UserRole.USER)
//                .build();
//        assertEquals(expected, actual);
//    }
//}
