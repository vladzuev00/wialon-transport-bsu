//package by.bsu.wialontransport.protocol.core.handler.packages.location;
//
//import by.bsu.wialontransport.config.property.LocationDefaultProperty;
//import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
//import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.OptionalDouble;
//import java.util.OptionalInt;
//
//public abstract class LocationPackageHandler<LOCATION_SOURCE, REQUEST> extends PackageHandler<REQUEST> {
//    private final LocationDefaultProperty locationDefaultProperty;
//    private final ContextAttributeManager contextAttributeManager;
//    private final LocationValidator locationValidator;
//    private final KafkaInboundLocationProducer locationProducer;
//
//    public LocationPackageHandler(final Class<REQUEST> requestType) {
//        super(requestType);
//    }
//
//    @Override
//    protected final Object handleInternal(final REQUEST request, final ChannelHandlerContext context) {
//        getLocationSources()
//                .stream()
//                .filter()
//        return null;
//    }
//
//    protected abstract List<LOCATION_SOURCE> getLocationSources();
//
//    protected abstract Optional<LocalDate> getDate(final LOCATION_SOURCE source);
//
//    protected abstract Optional<LocalTime> getTime(final LOCATION_SOURCE source);
//
//    protected abstract OptionalDouble getLatitude(final LOCATION_SOURCE source);
//
//    protected abstract OptionalDouble getLongitude(final LOCATION_SOURCE source);
//
//    protected abstract OptionalInt getCourse(final LOCATION_SOURCE source);
//
//
//}
