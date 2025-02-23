package by.vladzuev.locationreceiver.service.registration.model;

import by.vladzuev.locationreceiver.crud.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class RegisteredUserResponse {
    Long id;
    String email;
    UserRole role;
}
