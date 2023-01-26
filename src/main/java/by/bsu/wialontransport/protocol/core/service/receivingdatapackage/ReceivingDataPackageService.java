package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.reduceddata.RequestReducedDataPackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ReceivingDataPackageService {
    private final ContextAttributeManager contextAttributeManager;

    public void receive(final RequestReducedDataPackage reducedPackage, final ChannelHandlerContext context) {
        final Optional<ExtendedData> optionalPreviousExtendedData = this.contextAttributeManager
                .findLastExtendedData(context);

    }
}
