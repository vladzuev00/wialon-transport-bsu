package by.bsu.wialontransport.protocol.core.handler.packages.location;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import io.netty.channel.ChannelHandlerContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static java.time.LocalDateTime.MIN;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public abstract class LocationPackageHandler<LOCATION_SOURCE, REQUEST> extends PackageHandler<REQUEST> {
    private static final Comparator<Location> DATE_TIME_COMPARATOR = comparing(Location::getDateTime);

    private final ContextAttributeManager contextAttributeManager;
    private final LocationDefaultProperty locationDefaultProperty;
    private final LocationValidator locationValidator;
    private final KafkaInboundLocationProducer locationProducer;

    public LocationPackageHandler(final Class<REQUEST> requestType,
                                  final ContextAttributeManager contextAttributeManager,
                                  final LocationDefaultProperty locationDefaultProperty,
                                  final LocationValidator locationValidator,
                                  final KafkaInboundLocationProducer locationProducer) {
        super(requestType);
        this.contextAttributeManager = contextAttributeManager;
        this.locationDefaultProperty = locationDefaultProperty;
        this.locationValidator = locationValidator;
        this.locationProducer = locationProducer;
    }

    @Override
    protected final Object handleInternal(final REQUEST request, final ChannelHandlerContext context) {
        final List<LOCATION_SOURCE> locationSources = getLocationSources(request);
        handleLocations(locationSources, context);
        final int locationCount = locationSources.size();
        return createResponse(locationCount);
    }

    protected abstract List<LOCATION_SOURCE> getLocationSources(final REQUEST request);

    protected abstract Optional<LocalDate> findDate(final LOCATION_SOURCE source);

    protected abstract Optional<LocalTime> findTime(final LOCATION_SOURCE source);

    protected abstract OptionalDouble findLatitude(final LOCATION_SOURCE source);

    protected abstract OptionalDouble findLongitude(final LOCATION_SOURCE source);

    protected abstract OptionalInt findCourse(final LOCATION_SOURCE source);

    protected abstract OptionalDouble findSpeed(final LOCATION_SOURCE source);

    protected abstract OptionalInt findAltitude(final LOCATION_SOURCE source);

    protected abstract OptionalInt findSatelliteCount(final LOCATION_SOURCE source);

    protected abstract OptionalDouble findHdop(final LOCATION_SOURCE source);

    protected abstract OptionalInt findInputs(final LOCATION_SOURCE source);

    protected abstract OptionalInt findOutputs(final LOCATION_SOURCE source);

    protected abstract double[] getAnalogInputs(final LOCATION_SOURCE source);

    protected abstract Optional<String> findDriverKeyCode(final LOCATION_SOURCE source);

    protected abstract Stream<Parameter> getParameters(final LOCATION_SOURCE source);

    protected abstract Object createResponse(final int locationCount);

    private void handleLocations(final List<LOCATION_SOURCE> sources, final ChannelHandlerContext context) {
        final Tracker tracker = getTracker(context);
        final LocalDateTime lastReceivingDateTime = getLastReceivingDateTime(context);
        sources.stream()
                .map(source -> createLocation(source, tracker))
                .filter(locationValidator::isValid)
                .sorted(DATE_TIME_COMPARATOR)
                .dropWhile(location -> location.getDateTime().isBefore(lastReceivingDateTime))
                .peek(locationProducer::produce)
                .reduce((first, second) -> second)
                .ifPresent(lastLocation -> contextAttributeManager.putLastLocation(context, lastLocation));
    }

    private Tracker getTracker(final ChannelHandlerContext context) {
        return contextAttributeManager.findTracker(context)
                .orElseThrow(() -> new IllegalArgumentException("No tracker in context"));
    }

    private LocalDateTime getLastReceivingDateTime(final ChannelHandlerContext context) {
        return contextAttributeManager.findLastLocation(context)
                .map(Location::getDateTime)
                .orElse(MIN);
    }

    private Location createLocation(final LOCATION_SOURCE source, final Tracker tracker) {
        return Location.builder()
                .dateTime(getDateTime(source))
                .coordinate(getCoordinate(source))
                .course(getCourse(source))
                .speed(getSpeed(source))
                .altitude(getAltitude(source))
                .satelliteCount(getSatelliteCount(source))
                .hdop(getHdop(source))
                .inputs(getInputs(source))
                .outputs(getOutputs(source))
                .analogInputs(getAnalogInputs(source))
                .driverKeyCode(getDriverKeyCode(source))
                .parametersByNames(getParametersByNames(source))
                .tracker(tracker)
                .build();
    }

    private LocalDateTime getDateTime(final LOCATION_SOURCE source) {
        final LocalDate date = findDate(source).orElseGet(locationDefaultProperty::getDate);
        final LocalTime time = findTime(source).orElseGet(locationDefaultProperty::getTime);
        return LocalDateTime.of(date, time);
    }

    private GpsCoordinate getCoordinate(final LOCATION_SOURCE source) {
        final double latitude = findLatitude(source).orElse(locationDefaultProperty.getLatitude());
        final double longitude = findLongitude(source).orElse(locationDefaultProperty.getLongitude());
        return new GpsCoordinate(latitude, longitude);
    }

    private int getCourse(final LOCATION_SOURCE source) {
        return findCourse(source).orElse(locationDefaultProperty.getCourse());
    }

    private double getSpeed(final LOCATION_SOURCE source) {
        return findSpeed(source).orElse(locationDefaultProperty.getSpeed());
    }

    private int getAltitude(final LOCATION_SOURCE source) {
        return findAltitude(source).orElse(locationDefaultProperty.getAltitude());
    }

    private int getSatelliteCount(final LOCATION_SOURCE source) {
        return findSatelliteCount(source).orElse(locationDefaultProperty.getSatelliteCount());
    }

    private double getHdop(final LOCATION_SOURCE source) {
        return findHdop(source).orElse(locationDefaultProperty.getHdop());
    }

    private int getInputs(final LOCATION_SOURCE source) {
        return findInputs(source).orElse(locationDefaultProperty.getInputs());
    }

    private int getOutputs(final LOCATION_SOURCE source) {
        return findOutputs(source).orElse(locationDefaultProperty.getOutputs());
    }

    private String getDriverKeyCode(final LOCATION_SOURCE source) {
        return findDriverKeyCode(source).orElse(locationDefaultProperty.getDriverKeyCode());
    }

    private Map<String, Parameter> getParametersByNames(final LOCATION_SOURCE source) {
        return getParameters(source).collect(toMap(Parameter::getName, identity()));
    }
}
