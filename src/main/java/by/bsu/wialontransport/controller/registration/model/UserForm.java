package by.bsu.wialontransport.controller.registration.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class UserForm {

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+", message = "not valid email")
    private String email;

    @NotNull
    @Size(min = 5, max = 30)
    private String password;

    @NotNull
    @Size(min = 5, max = 30)
    private String confirmedPassword;
}