package by.bsu.wialontransport.protocol.core.model;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.property.ProtocolServerProperty;
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
