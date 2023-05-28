package by.bsu.wialontransport.controller.registration.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class UserForm {
    private String email;
    private String password;
    private String confirmedPassword;
}
