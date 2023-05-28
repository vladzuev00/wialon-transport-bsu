package by.bsu.wialontransport.model;

public enum RegistrationStatus {
    SUCCESS("redirect:/"),
    BINDING_ERROR("registration"),
    CONFIRMING_PASSWORD_ERROR("registration"),
    EMAIL_ALREADY_EXIST("registration");

    private final String viewName;

    RegistrationStatus(final String viewName) {
        this.viewName = viewName;
    }

    public final String getViewName() {
        return this.viewName;
    }
}
