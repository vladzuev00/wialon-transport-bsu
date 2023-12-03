package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.transportable.TransportableSavedData;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaSavedDataProducer extends AbstractKafkaDataProducer<TransportableSavedData> {

    public KafkaSavedDataProducer(@Qualifier("kafkaTemplateSavedData") final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                  @Value("${kafka.topic.saved-data.name}") final String topicName,
                                  @Qualifier("transportableSavedDataSchema") final Schema schema) {
        super(kafkaTemplate, topicName, schema);
    }


    @Override
    protected TransportableSavedData mapToTransportable(final Data data) {
//        return new TransportableSavedData(
//                data.getId(),
//                findEpochSeconds(data),
//                findLatitudeDegrees(data),
//                findLatitudeMinutes(data),
//                findLatitudeMinuteShare(data),
//                findLatitudeTypeValue(data),
//                findLongitudeDegrees(data),
//                findLongitudeMinutes(data),
//                findLongitudeMinuteShare(data),
//                findLongitudeTypeValue(data),
//                data.getSpeed(),
//                data.getCourse(),
//                data.getAltitude(),
//                data.getAmountOfSatellites(),
//                data.getReductionPrecision(),
//                data.getInputs(),
//                data.getOutputs(),
//                super.serializeAnalogInputs(data),
//                data.getDriverKeyCode(),
//                super.serializeParameters(data),
//                findTrackerId(data),
//                findAddressId(data)
//        );
        return null;
    }

    private static Long findAddressId(final Data data) {
        final Address address = data.getAddress();
        return address.getId();
    }
}
