package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdatapackage.exception.NoTrackerInContextException;
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
    private final DataCalculationsFactory dataCalculationsFactory;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;

    public void receive(final Data receivedData, final ChannelHandlerContext context) {
        final Tracker tracker = this.findTracker(context);
        final Optional<Data> optionalPreviousData = this.contextAttributeManager.findLastData(context);
        optionalPreviousData
                .ifPresentOrElse(previousData -> {
                            if (this.dataValidator.isValid(receivedData, previousData)) {
                                final DataCalculations dataCalculations = this.dataCalculationsFactory
                                        .create(receivedData, previousData);
                                final Data receivedDataWithCalculations = new Data(receivedData, dataCalculations);
                                this.contextAttributeManager.putLastData(context, receivedDataWithCalculations);
                                this.kafkaInboundDataProducer.send(receivedDataWithCalculations, tracker);
                            } else if (this.dataValidator.isNeededToBeFixed(receivedData, previousData)) {
                                final Data fixedReceivedData = fixData(receivedData, previousData);
                                final DataCalculations dataCalculations = this.dataCalculationsFactory
                                        .create(fixedReceivedData, previousData);
                                final Data fixedReceivedDataWithCalculations = new Data(fixedReceivedData, dataCalculations);
                                this.contextAttributeManager.putLastData(context, fixedReceivedDataWithCalculations);
                                this.kafkaInboundDataProducer.send(fixedReceivedDataWithCalculations, tracker);
                            }
                        },
                        () -> {
                            if (this.dataValidator.isValid(receivedData)) {
                                final DataCalculations dataCalculations = this.dataCalculationsFactory
                                        .create(receivedData);
                                final Data receivedDataWithCalculations = new Data(receivedData, dataCalculations);
                                this.contextAttributeManager.putLastData(context, receivedDataWithCalculations);
                                this.kafkaInboundDataProducer.send(receivedDataWithCalculations, tracker);
                            }
                        });
    }

    private Tracker findTracker(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        return optionalTracker.orElseThrow(NoTrackerInContextException::new);
    }

    private static Data fixData(final Data fixed, final Data previous) {
        return Data.builder()
                .id(fixed.getId())
                .date(fixed.getDate())
                .time(fixed.getTime())
                .latitude(previous.getLatitude())
                .longitude(previous.getLongitude())
                .speed(fixed.getSpeed())
                .course(fixed.getCourse())
                .altitude(fixed.getAltitude())
                .amountOfSatellites(previous.getAmountOfSatellites())
                .reductionPrecision(fixed.getReductionPrecision())
                .inputs(fixed.getInputs())
                .outputs(fixed.getOutputs())
                .analogInputs(fixed.getAnalogInputs())
                .driverKeyCode(fixed.getDriverKeyCode())
                .parametersByNames(fixed.getParametersByNames())
                .build();
    }
}
