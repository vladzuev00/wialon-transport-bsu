package by.vladzuev.locationreceiver.kafka.consumer.data;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.kafka.model.view.SavedParameterView;
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
    private final AddressService addressService;

    public KafkaSavedDataConsumer(final ObjectMapper objectMapper,
                                  final TrackerService trackerService,
                                  final AddressService addressService) {
        super(objectMapper, trackerService, SavedParameterView.class);
        this.addressService = addressService;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.saved-data.name}",
            groupId = "${kafka.topic.saved-data.consumer.group-id}",
            containerFactory = "listenerContainerFactorySavedData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> records) {
        super.consume(records);
    }

    @Override
    protected Location createData(final ConsumingContext context) {
        return Location.builder()
                .id(extractDataId(context))
                .dateTime(context.getDateTime())
                .coordinate(context.getCoordinate())
                .course(context.getCourse())
                .speed(context.getSpeed())
                .altitude(context.getAltitude())
                .satelliteCount(context.getAmountOfSatellites())
                .hdop(context.getHdop())
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
    protected Optional<Address> findSavedAddress(final ConsumingContext context) {
        return addressService.findById(extractAddressId(context));
    }

    @Override
    protected void process(final List<Location> data) {
        log.info("Consuming saved data: {}", data);
    }

    private static Long extractDataId(final ConsumingContext context) {
        return extractProperty(
                context,
                ConsumingContext::getDataId,
                "Consumer should consume only already saved data"
        );
    }

    private static Long extractAddressId(final ConsumingContext context) {
        return extractProperty(
                context,
                ConsumingContext::getAddressId,
                "Consumer should consume data only with already saved address"
        );
    }

    private static <T> T extractProperty(final ConsumingContext context,
                                         final Function<ConsumingContext, Optional<T>> getter,
                                         final String noSuchPropertyExceptionMessage) {
        return getter.apply(context).orElseThrow(() -> new ConsumingException(noSuchPropertyExceptionMessage));
    }
}
