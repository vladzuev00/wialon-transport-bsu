package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator.DataValidator;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ReceivingDataPackageService {
    private final ContextAttributeManager contextAttributeManager;
    private final DataValidator dataValidator;

    public void receive(final Data receivedData, final ChannelHandlerContext context) {
        final Optional<DataCalculations> optionalPreviousDataCalculations = this.contextAttributeManager
                .findLastDataCalculations(context);
        final Optional<DataCalculations> newLastDataCalculations = optionalPreviousDataCalculations
                .map(DataCalculations::getData)
                .map(previousData -> {
                    if (this.dataValidator.isValid(receivedData, previousData)) {
                        
                    }
                })
    }


}
