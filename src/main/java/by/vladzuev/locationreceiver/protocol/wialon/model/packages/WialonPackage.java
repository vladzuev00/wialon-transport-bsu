package by.vladzuev.locationreceiver.protocol.wialon.model.packages;

public interface WialonPackage {
    String POSTFIX = "\r\n";

    String getPrefix();
}
