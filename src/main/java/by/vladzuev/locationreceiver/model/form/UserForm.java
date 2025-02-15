package by.vladzuev.locationreceiver.model.form;

import by.vladzuev.locationreceiver.validation.annotation.Password;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public final class UserForm {

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+", message = "not valid email")
    private String email;

    @Password
    private String password;

    @Password
    private String confirmedPassword;
}
