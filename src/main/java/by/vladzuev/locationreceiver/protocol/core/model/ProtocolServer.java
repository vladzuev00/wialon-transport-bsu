package by.vladzuev.locationreceiver.protocol.core.model;

import by.vladzuev.locationreceiver.protocol.core.decoder.ProtocolDecoder;
import by.vladzuev.locationreceiver.protocol.core.encoder.ProtocolEncoder;
import by.vladzuev.locationreceiver.protocol.core.handler.ProtocolHandler;
import by.vladzuev.locationreceiver.protocol.core.property.ProtocolServerProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ProtocolServer {
    private final ProtocolDecoder<?> decoder;
    private final ProtocolHandler handler;
    private final ProtocolEncoder encoder;
    private final ProtocolServerProperty property;
}
