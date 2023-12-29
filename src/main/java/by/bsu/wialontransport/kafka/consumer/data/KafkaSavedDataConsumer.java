package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.model.view.SavedParameterView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public final class KafkaSavedDataConsumer extends KafkaDataConsumer<SavedParameterView> {

    public KafkaSavedDataConsumer(final ObjectMapper objectMapper,
                                  final TrackerService trackerService,
                                  final AddressService addressService) {
        super(objectMapper, trackerService, addressService, SavedParameterView.class);
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.saved-data.name}",
            groupId = "${kafka.topic.saved-data.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactorySavedData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> records) {
        super.consume(records);
    }

    @Override
    protected Data createData(final DataCreatingContext context) {
        return Data.builder()
                .id(extractDataId(context))
                .dateTime(context.getDateTime())
                .coordinate(context.getCoordinate())
                .course(context.getCourse())
                .speed(context.getSpeed())
                .altitude(context.getAltitude())
                .amountOfSatellites(context.getAmountOfSatellites())
                .reductionPrecision(context.getReductionPrecision())
                .inputs(context.getInputs())
                .outputs(context.getOutputs())
                .analogInputs(context.getAnalogInputs())
                .driverKeyCode(context.getDriverKeyCode())
                .parametersByNames(context.getParametersByNames())
                .tracker(context.getTracker())
                .address(context.getAddress())
                .build();
    }

    @Override
    protected Parameter createParameter(final SavedParameterView view) {
        return Parameter.builder()
                .name(view.getName())
                .type(view.getType())
                .value(view.getValue())
                .id(view.getId())
                .build();
    }

    @Override
    protected Optional<Address> findAddress(final DataCreatingContext context, final AddressService addressService) {
        return addressService.findById(extractAddressId(context));
    }

    @Override
    protected void process(final List<Data> data) {
        log.info("Consuming saved data: {}", data);
    }

    private static Long extractDataId(final DataCreatingContext context) {
        return extractProperty(
                context,
                DataCreatingContext::getDataId,
                "Consumer should consume only already saved data"
        );
    }

    private static Long extractAddressId(final DataCreatingContext context) {
        return extractProperty(
                context,
                DataCreatingContext::getAddressId,
                "Consumer should consume data only with already saved address"
        );
    }

    private static <T> T extractProperty(final DataCreatingContext context,
                                         final Function<DataCreatingContext, Optional<T>> getter,
                                         final String noSuchPropertyExceptionMessage) {
        return getter.apply(context).orElseThrow(() -> new IllegalStateException(noSuchPropertyExceptionMessage));
    }
}
