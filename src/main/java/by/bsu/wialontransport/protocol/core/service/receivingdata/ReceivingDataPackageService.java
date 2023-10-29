package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;

@Service
public final class ReceivingDataPackageService
        extends AbstractReceivingDataPackageService<WialonRequestDataPackage, WialonResponseDataPackage> {
    private static final int AMOUNT_OF_RECEIVED_DATA_OF_SUCCESS = 1;

    public ReceivingDataPackageService(final ContextAttributeManager contextAttributeManager,
                                       final DataFilter dataFilter,
                                       final KafkaInboundDataProducer kafkaInboundDataProducer,
                                       final DataFixer dataFixer) {
        super(contextAttributeManager, dataFilter, kafkaInboundDataProducer, dataFixer);
    }


    @Override
    protected WialonResponseDataPackage createResponse(final List<Data> receivedData) {
        return receivedData.size() == AMOUNT_OF_RECEIVED_DATA_OF_SUCCESS
                ? new WialonResponseDataPackage(PACKAGE_FIX_SUCCESS)
                : new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
    }
}
