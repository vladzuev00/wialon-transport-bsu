package by.vladzuev.locationreceiver.model.form;

import by.vladzuev.locationreceiver.validation.annotation.Password;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ChangePasswordForm {

    @Password
    private String oldPassword;

    @Password
    private String newPassword;
    
    @Password
    private String confirmedNewPassword;

}
