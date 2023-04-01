package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.AbstractKafkaDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class ReceivingBlackBoxPackageService
        extends AbstractReceivingDataPackageService<RequestBlackBoxPackage, ResponseBlackBoxPackage> {

    public ReceivingBlackBoxPackageService(final ContextAttributeManager contextAttributeManager,
                                           final DataFilter dataFilter,
                                           final AbstractKafkaDataProducer kafkaInboundDataProducer,
                                           final DataFixer dataFixer) {
        super(contextAttributeManager, dataFilter, kafkaInboundDataProducer, dataFixer);
    }

    @Override
    protected ResponseBlackBoxPackage createResponse(final List<Data> receivedData) {
        return new ResponseBlackBoxPackage(receivedData.size());
    }
}
