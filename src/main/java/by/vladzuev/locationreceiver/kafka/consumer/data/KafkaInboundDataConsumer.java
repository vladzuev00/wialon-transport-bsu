package by.vladzuev.locationreceiver.kafka.consumer.data;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.kafka.model.view.InboundParameterView;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaSavedDataProducer;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.Track;
import by.vladzuev.locationreceiver.service.geocoding.GeocodingService;
import by.vladzuev.locationreceiver.service.mileage.MileageIncreasingService;
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
    private final GeocodingService geocodingService;
    private final LocationService dataService;
    private final MileageIncreasingService mileageIncreasingService;
    private final KafkaSavedDataProducer savedDataProducer;

    public KafkaInboundDataConsumer(final ObjectMapper objectMapper,
                                    final TrackerService trackerService,
                                    final GeocodingService geocodingService,
                                    final LocationService dataService,
                                    final MileageIncreasingService mileageIncreasingService,
                                    final KafkaSavedDataProducer savedDataProducer) {
        super(objectMapper, trackerService, InboundParameterView.class);
        this.geocodingService = geocodingService;
        this.dataService = dataService;
        this.mileageIncreasingService = mileageIncreasingService;
        this.savedDataProducer = savedDataProducer;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-data.name}",
            groupId = "${kafka.topic.inbound-data.consumer.group-id}",
            containerFactory = "listenerContainerFactoryInboundData",
            concurrency = "1"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> records) {
        super.consume(records);
    }

    @Override
    protected Location createData(final ConsumingContext context) {
        return Location.builder()
                .dateTime(context.getDateTime())
                .coordinate(context.getCoordinate())
                .speed(context.getSpeed())
                .course(context.getCourse())
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
    protected Parameter createParameter(final InboundParameterView view) {
        return Parameter.builder()
                .name(view.getName())
                .type(view.getType())
                .value(view.getValue())
                .build();
    }

    @Override
    protected Optional<Address> findSavedAddress(final ConsumingContext context) {
//TODO
//         private Address saveIfNew(final Address address) {
//        if (nonNull(address.getId())) {
//            return address;
//        }
//        return addressService.findByGeometry(address.getGeometry()).orElseGet(() -> addressService.save(address));
//    }
        return geocodingService.geocode(context.getCoordinate());
    }

    @Override
    @Transactional
    protected void process(final List<Location> data) {
        increaseMileages(data);
        final List<Location> savedData = dataService.saveAll(data);
        sendToSavedDataTopic(savedData);
    }

    private void increaseMileages(final List<Location> data) {
        groupCoordinatesByTrackers(data)
                .entrySet()
                .stream()
                .map(coordinatesByTracker -> new Track(coordinatesByTracker.getKey(), coordinatesByTracker.getValue()))
                .forEach(mileageIncreasingService::increase);
    }

    private static Map<Tracker, List<GpsCoordinate>> groupCoordinatesByTrackers(final List<Location> data) {
        return data.stream()
                .collect(
                        groupingBy(
                                Location::getTracker,
                                mapping(Location::getCoordinate, toList())
                        )
                );
    }

    private void sendToSavedDataTopic(final List<Location> data) {
        data.forEach(savedDataProducer::produce);
    }
}
