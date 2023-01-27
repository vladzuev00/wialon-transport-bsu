package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.ChannelData;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ReceivingDataPackageService {
    private final ContextAttributeManager contextAttributeManager;

    public void receive(final Data receivedData, final ChannelHandlerContext context) {
        final Optional<ChannelData> optionalPreviousChannelData = this.contextAttributeManager
                .findLastChannelData(context);

    }
}
