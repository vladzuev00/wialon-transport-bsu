package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class ReceivingBlackBoxPackageService
        extends AbstractReceivingDataPackageService<WialonRequestBlackBoxPackage, WialonResponseBlackBoxPackage> {

    public ReceivingBlackBoxPackageService(final ContextAttributeManager contextAttributeManager,
                                           final DataFilter dataFilter,
                                           final KafkaInboundDataProducer kafkaInboundDataProducer,
                                           final DataFixer dataFixer) {
        super(contextAttributeManager, dataFilter, kafkaInboundDataProducer, dataFixer);
    }

    @Override
    protected WialonResponseBlackBoxPackage createResponse(final List<Data> receivedData) {
        return new WialonResponseBlackBoxPackage(receivedData.size());
    }
}
