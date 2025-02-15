package by.vladzuev.locationreceiver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RegistrationStatus {
    SUCCESS("redirect:/"),
    BINDING_ERROR("registration"),
    CONFIRMING_PASSWORD_ERROR("registration"),
    EMAIL_ALREADY_EXISTS("registration");

    private final String viewName;
}
