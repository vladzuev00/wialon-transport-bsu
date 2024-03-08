package by.bsu.wialontransport.service.registration.model;

import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class RegisteredUserResponse {
    Long id;
    String email;
    Role role;
}
