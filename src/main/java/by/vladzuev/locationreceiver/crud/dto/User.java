package by.vladzuev.locationreceiver.crud.dto;

import by.vladzuev.locationreceiver.crud.entity.UserEntity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class User implements Dto<Long> {
    Long id;
    String email;
    String password;
    UserRole role;
}
