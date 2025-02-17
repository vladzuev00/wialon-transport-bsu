package by.vladzuev.locationreceiver.model.form;

import by.vladzuev.locationreceiver.validation.annotation.Password;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class TrackerForm {
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{20}", message = "should contain only 20 digits")
    private String imei;

    @Password
    private String password;

    @NotNull
    @Pattern(regexp = "\\d{9}", message = "should contain only 9 digits")
    private String phoneNumber;
}
