package by.bsu.wialontransport.model.form;

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
@Builder
public class TrackerForm {
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{20}", message = "should contain only 20 digits")
    private String imei;

    @NotNull
    @Size(min = 5, max = 128)
    private String password;

    @NotNull
    @Pattern(regexp = "\\d{9}", message = "should contain only 9 digits")
    private String phoneNumber;
}
