package by.bsu.wialontransport.service.registration.model;

import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.Value;

@Value
public class RegisteredUserResponse {
    Long id;
    String email;
    Role role;
}
