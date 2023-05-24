package by.bsu.wialontransport.model;

public enum SignInStatus {
    SUCCESS(""), WRONG_EMAIL(""), WRONG_PASSWORD("");

    private final String nameOfView;

    SignInStatus(final String nameOfView) {
        this.nameOfView = nameOfView;
    }

    public String getNameOfView() {
        return this.nameOfView;
    }
}
