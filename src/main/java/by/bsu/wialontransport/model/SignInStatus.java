package by.bsu.wialontransport.model;

public enum SignInStatus {
    SUCCESS(""), WRONG_EMAIL(""), WRONG_PASSWORD("");

    private final String viewName;

    SignInStatus(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return this.viewName;
    }
}
