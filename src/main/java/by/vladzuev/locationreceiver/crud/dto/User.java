package by.vladzuev.locationreceiver.crud.dto;

import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class User implements Dto<Long> {
    Long id;
    String email;
    String password;
    UserEntity.Role role;
}
