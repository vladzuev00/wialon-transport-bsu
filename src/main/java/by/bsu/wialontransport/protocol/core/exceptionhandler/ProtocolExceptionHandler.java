package by.bsu.wialontransport.protocol.core.exceptionhandler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

//TODO: realize
@RequiredArgsConstructor
public final class ProtocolExceptionHandler extends ChannelInboundHandlerAdapter {
    private final ContextAttributeManager contextAttributeManager;
}
