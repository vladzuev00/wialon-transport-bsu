package by.bsu.wialontransport.kafka.consumer.data;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.KafkaGenericRecordConsumer;
import by.bsu.wialontransport.kafka.model.view.ParameterView;
import by.bsu.wialontransport.model.GpsCoordinate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static by.bsu.wialontransport.kafka.model.transportable.data.TransportableData.Fields.*;
import static by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData.Fields.addressId;
import static by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData.Fields.id;
import static by.bsu.wialontransport.util.StreamUtil.toStream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public abstract class KafkaDataConsumer<P extends ParameterView> extends KafkaGenericRecordConsumer<Long, Location> {
    private final ObjectMapper objectMapper;
    private final TrackerService trackerService;
    private final Class<P> parameterViewType;

    @Override
    protected final Location mapToSource(final GenericRecord record) {
        final ConsumingContext context = new ConsumingContext(record);
        return createData(context);
    }

    protected abstract Location createData(final ConsumingContext context);

    protected abstract Parameter createParameter(final P view);

    protected abstract Optional<Address> findSavedAddress(final ConsumingContext context);

    @RequiredArgsConstructor
    protected final class ConsumingContext {
        private final GenericRecord record;

        public Optional<Long> getDataId() {
            return getNullable(() -> extractValue(record, id, Long.class));
        }

        public LocalDateTime getDateTime() {
            return extractDateTime(record, epochSeconds);
        }

        public GpsCoordinate getCoordinate() {
            return new GpsCoordinate(
                    extractLatitude(),
                    extractLongitude()
            );
        }

        public int getCourse() {
            return extractValue(record, course, Integer.class);
        }

        public double getSpeed() {
            return extractValue(record, speed, Double.class);
        }

        public int getAltitude() {
            return extractValue(record, altitude, Integer.class);
        }

        public int getAmountOfSatellites() {
            return extractValue(record, amountOfSatellites, Integer.class);
        }

        public double getHdop() {
            return extractValue(record, hdop, Double.class);
        }

        public int getInputs() {
            return extractValue(record, inputs, Integer.class);
        }

        public int getOutputs() {
            return extractValue(record, outputs, Integer.class);
        }

        public double[] getAnalogInputs() {
            return getSerializedProperty(
                    record,
                    serializedAnalogInputs,
                    source -> objectMapper.readValue(source, double[].class)
            );
        }

        public String getDriverKeyCode() {
            return extractString(record, driverKeyCode);
        }

        public Map<String, Parameter> getParametersByNames() {
            return deserializeParameterViews()
                    .map(KafkaDataConsumer.this::createParameter)
                    .collect(
                            toMap(
                                    Parameter::getName,
                                    identity()
                            )
                    );
        }

        public Tracker getTracker() {
            final Long id = extractTrackerId();
            return trackerService.findById(id)
                    .orElseThrow(
                            () -> new ConsumingException(
                                    "There is no tracker with id '%s'".formatted(id)
                            )
                    );
        }

        public Optional<Long> getAddressId() {
            return getNullable(() -> extractValue(record, addressId, Long.class));
        }

        public Address getAddress() {
            return findSavedAddress(this).orElseThrow(() -> new ConsumingException("Impossible to find address"));
        }

        private double extractLatitude() {
            return extractValue(record, latitude, Double.class);
        }

        private double extractLongitude() {
            return extractValue(record, longitude, Double.class);
        }

        private static <T> T getSerializedProperty(final GenericRecord record,
                                                   final String key,
                                                   final PropertyDeserializer<T> deserializer) {
            try {
                final String serialized = extractString(record, key);
                return deserializer.deserialize(serialized);
            } catch (final IOException cause) {
                throw new ConsumingException(cause);
            }
        }

        private Stream<P> deserializeParameterViews() {
            return toStream(
                    getSerializedProperty(
                            record,
                            serializedParameters,
                            source -> objectMapper.readerFor(parameterViewType).readValues(source)
                    )
            );
        }

        private Long extractTrackerId() {
            return extractValue(record, trackerId, Long.class);
        }

        private <T> Optional<T> getNullable(final Supplier<T> supplier) {
            return ofNullable(supplier.get());
        }

        @FunctionalInterface
        private interface PropertyDeserializer<T> {
            T deserialize(final String source) throws IOException;
        }
    }

    static final class ConsumingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ConsumingException() {

        }

        @SuppressWarnings("unused")
        public ConsumingException(final String description) {
            super(description);
        }

        public ConsumingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ConsumingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
