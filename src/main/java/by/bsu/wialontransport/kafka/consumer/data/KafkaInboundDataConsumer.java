package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.model.view.InboundParameterView;
import by.bsu.wialontransport.kafka.producer.data.KafkaSavedDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.geocoding.GeocodingManager;
import by.bsu.wialontransport.service.mileage.MileageIncreasingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Component
public class KafkaInboundDataConsumer extends KafkaDataConsumer<InboundParameterView> {
    private final GeocodingManager geocodingManager;
    private final DataService dataService;
    private final MileageIncreasingService mileageIncreasingService;
    private final KafkaSavedDataProducer savedDataProducer;

    public KafkaInboundDataConsumer(final ObjectMapper objectMapper,
                                    final TrackerService trackerService,
                                    final GeocodingManager geocodingManager,
                                    final DataService dataService,
                                    final MileageIncreasingService mileageIncreasingService,
                                    final KafkaSavedDataProducer savedDataProducer) {
        super(objectMapper, trackerService, InboundParameterView.class);
        this.geocodingManager = geocodingManager;
        this.dataService = dataService;
        this.mileageIncreasingService = mileageIncreasingService;
        this.savedDataProducer = savedDataProducer;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-data.name}",
            groupId = "${kafka.topic.inbound-data.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryInboundData",
            concurrency = "1"
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
    protected Optional<Address> findSavedAddress(final ConsumingContext context) {
        return geocodingManager.findSavedAddress(context.getCoordinate());
    }

    @Override
    @Transactional
    protected void process(final List<Data> data) {
        increaseMileages(data);
        final List<Data> savedData = dataService.saveAll(data);
        sendToSavedDataTopic(savedData);
    }

    private void increaseMileages(final List<Data> data) {
        groupCoordinatesByTrackers(data)
                .entrySet()
                .stream()
                .map(coordinatesByTracker -> new Track(coordinatesByTracker.getKey(), coordinatesByTracker.getValue()))
                .forEach(mileageIncreasingService::increase);
    }

    private static Map<Tracker, List<Coordinate>> groupCoordinatesByTrackers(final List<Data> data) {
        return data.stream()
                .collect(
                        groupingBy(
                                Data::getTracker,
                                mapping(Data::getCoordinate, toList())
                        )
                );
    }

    private void sendToSavedDataTopic(final List<Data> data) {
        data.forEach(savedDataProducer::send);
    }
}
