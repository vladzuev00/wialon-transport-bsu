package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.AbstractKafkaDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;

@Service
public final class ReceivingDataPackageService
        extends AbstractReceivingDataPackageService<RequestDataPackage, ResponseDataPackage> {
    private static final int AMOUNT_OF_RECEIVED_DATA_OF_SUCCESS = 1;

    public ReceivingDataPackageService(final ContextAttributeManager contextAttributeManager,
                                       final DataFilter dataFilter,
                                       final AbstractKafkaDataProducer kafkaInboundDataProducer,
                                       final DataFixer dataFixer) {
        super(contextAttributeManager, dataFilter, kafkaInboundDataProducer, dataFixer);
    }


    @Override
    protected ResponseDataPackage createResponse(final List<Data> receivedData) {
        return receivedData.size() == AMOUNT_OF_RECEIVED_DATA_OF_SUCCESS
                ? new ResponseDataPackage(PACKAGE_FIX_SUCCESS)
                : new ResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
    }
}
