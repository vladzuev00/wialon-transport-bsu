package by.vladzuev.locationreceiver.protocol.teltonika.model.login;

public final class TeltonikaResponseFailedLoginPackage extends TeltonikaResponseLoginPackage {
    private static final byte VALUE = 0;

    public TeltonikaResponseFailedLoginPackage() {
        super(VALUE);
    }
}
