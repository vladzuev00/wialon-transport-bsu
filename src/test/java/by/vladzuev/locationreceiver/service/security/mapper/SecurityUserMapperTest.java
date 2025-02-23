//package by.vladzuev.locationreceiver.service.security.mapper;
//
//import by.vladzuev.locationreceiver.crud.dto.User;
//import by.vladzuev.locationreceiver.service.security.model.SecurityUser;
//import org.junit.Test;
//
//import static by.vladzuev.locationreceiver.crud.entity.UserEntity.UserRole.USER;
//import static org.junit.Assert.assertEquals;
//
//public final class SecurityUserMapperTest {
//    private final SecurityUserMapper mapper = new SecurityUserMapper();
//
//    @Test
//    public void securityUserShouldBeMappedToUser() {
//        final SecurityUser givenSecurityUser = SecurityUser.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(USER)
//                .build();
//
//        final User actual = mapper.map(givenSecurityUser);
//        final User expected = User.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(USER)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void userShouldBeMappedToSecurityUser() {
//        final User givenUser = User.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(USER)
//                .build();
//
//        final SecurityUser actual = mapper.map(givenUser);
//        final SecurityUser expected = SecurityUser.builder()
//                .id(255L)
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .role(USER)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//}
