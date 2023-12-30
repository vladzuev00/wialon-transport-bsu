package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.model.view.InboundParameterView;
import by.bsu.wialontransport.kafka.producer.data.KafkaSavedDataProducer;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public final class KafkaInboundDataConsumer extends KafkaDataConsumer<InboundParameterView> {
    private final DataService dataService;
    private final GeocodingService geocodingService;
    private final KafkaSavedDataProducer savedDataProducer;

    public KafkaInboundDataConsumer(final ObjectMapper objectMapper,
                                    final TrackerService trackerService,
                                    final AddressService addressService,
                                    final DataService dataService,
                                    @Qualifier("chainGeocodingService") final GeocodingService geocodingService,
                                    final KafkaSavedDataProducer savedDataProducer) {
        super(objectMapper, trackerService, addressService, InboundParameterView.class);
        this.dataService = dataService;
        this.geocodingService = geocodingService;
        this.savedDataProducer = savedDataProducer;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-data.name}",
            groupId = "${kafka.topic.inbound-data.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryInboundData",
            concurrency = "1"  //TODO: increase concurrency after doing operation finding address thread safe
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> records) {
        super.consume(records);
    }

    @Override
    protected Data createData(final ConsumingContext context) {
        return Data.builder()
                .dateTime(context.getDateTime())
                .coordinate(context.getCoordinate())
                .speed(context.getSpeed())
                .course(context.getCourse())
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
    protected Parameter createParameter(final InboundParameterView view) {
        return Parameter.builder()
                .name(view.getName())
                .type(view.getType())
                .value(view.getValue())
                .build();
    }

    @Override
    protected Optional<Address> findAddress(final ConsumingContext context, final AddressService addressService) {
        return geocodingService.receive(context.getCoordinate())
                .map(address -> mapToSavedAddress(address, addressService));
    }

    @Override
    protected void process(final List<Data> data) {
        final List<Data> savedData = dataService.saveAll(data);
        sendToSavedDataTopic(savedData);
    }

    private static Address mapToSavedAddress(final Address address, final AddressService addressService) {
        return address.isNew() ? addressService.save(address) : address;
    }

    private void sendToSavedDataTopic(final List<Data> data) {
        data.forEach(savedDataProducer::send);
    }
}
