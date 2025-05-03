package by.vladzuev.locationreceiver.protocol.teltonika.model.login;

public final class TeltonikaResponseSuccessLoginPackage extends TeltonikaResponseLoginPackage {
    private static final byte VALUE = 1;

    public TeltonikaResponseSuccessLoginPackage() {
        super(VALUE);
    }
}
