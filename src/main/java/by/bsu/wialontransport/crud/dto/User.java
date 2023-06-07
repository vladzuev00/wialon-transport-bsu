package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class User implements AbstractDto<Long> {
    Long id;
    String email;
    String password;
    Role role;
}
