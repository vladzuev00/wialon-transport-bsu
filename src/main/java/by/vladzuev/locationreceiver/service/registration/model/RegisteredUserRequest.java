package by.vladzuev.locationreceiver.service.registration.model;

import by.vladzuev.locationreceiver.validation.annotation.Email;
import by.vladzuev.locationreceiver.validation.annotation.Password;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

//TODO: rename to view and put next to RegistrationService
@Value
public class RegisteredUserRequest {

    @Email
    String email;

    @Password
    String password;

    @Password
    String confirmedPassword;

    @Builder
    @JsonCreator
    public RegisteredUserRequest(@JsonProperty("email") final String email,
                                 @JsonProperty("password") final String password,
                                 @JsonProperty("confirmedPassword") final String confirmedPassword) {
        this.email = email;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }
}
