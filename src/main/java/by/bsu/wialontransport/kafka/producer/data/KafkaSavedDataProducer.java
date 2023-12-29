package by.bsu.wialontransport.kafka.producer.data;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.model.view.SavedParameterView;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaSavedDataProducer extends KafkaDataProducer {

    public KafkaSavedDataProducer(@Qualifier("kafkaTemplateSavedData") final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                  @Value("${kafka.topic.saved-data.name}") final String topicName,
                                  @Qualifier("transportableSavedDataSchema") final Schema schema,
                                  final ObjectMapper objectMapper) {
        super(kafkaTemplate, topicName, schema, objectMapper);
    }

    @Override
    protected TransportableSavedData createTransportable(final CreatingTransportableContext context) {
        return TransportableSavedData.builder()
                .id(context.getDataId())
                .epochSeconds(context.getEpochSeconds())
                .latitude(context.getLatitude())
                .longitude(context.getLongitude())
                .course(context.getCourse())
                .speed(context.getSpeed())
                .altitude(context.getAltitude())
                .amountOfSatellites(context.getAmountOfSatellites())
                .reductionPrecision(context.getReductionPrecision())
                .inputs(context.getInputs())
                .outputs(context.getOutputs())
                .serializedAnalogInputs(context.getSerializedAnalogInputs())
                .driverKeyCode(context.getDriverKeyCode())
                .serializedParameters(context.getSerializedParameters())
                .trackerId(context.getTrackerId())
                .addressId(context.getAddressId())
                .build();
    }

    @Override
    protected SavedParameterView createParameterView(final Parameter parameter) {
        return SavedParameterView.builder()
                .name(parameter.getName())
                .type(parameter.getType())
                .value(parameter.getValue())
                .id(parameter.getId())
                .build();
    }
}
