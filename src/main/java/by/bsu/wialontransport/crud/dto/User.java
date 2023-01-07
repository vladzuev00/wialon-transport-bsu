package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.Value;

@Value
public class User implements AbstractDto<Long> {
    Long id;
    String email;
    String password;
    Role role;
}
