package by.bsu.wialontransport.protocol.core.server;

import by.bsu.wialontransport.config.property.protocolserver.ProtocolServerConfig;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class ProtocolServer {
    private final ProtocolDecoder decoder;
    private final ProtocolHandler handler;
    private final ProtocolEncoder encoder;
    private final ProtocolServerConfig config;
}
