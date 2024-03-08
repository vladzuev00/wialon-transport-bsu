package by.bsu.wialontransport.service.registration.model;

import by.bsu.wialontransport.validation.annotation.Email;
import by.bsu.wialontransport.validation.annotation.Password;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

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
