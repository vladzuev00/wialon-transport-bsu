package by.bsu.wialontransport.model.form;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ChangePasswordForm {

    @NotNull
    @Size(min = 5, max = 128)
    private String oldPassword;

    @NotNull
    @Size(min = 5, max = 128)
    private String newPassword;

    @NotNull
    @Size(min = 5, max = 128)
    private String confirmedNewPassword;

}
