package by.bsu.wialontransport.model.form;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class TrackerForm {
    Long id;

    @NotNull
    @Pattern(regexp = "\\d{20}", message = "should contain only 20 digits")
    String imei;

    @NotNull
    @Size(min = 5, max = 256)
    String password;

    @NotNull
    @Pattern(regexp = "\\d{9}", message = "should contain only 9 digits")
    String phoneNumber;
}
