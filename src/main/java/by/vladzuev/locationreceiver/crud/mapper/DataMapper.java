package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.util.CollectionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class DataMapper extends Mapper<LocationEntity, Location> {

    public DataMapper(final ModelMapper modelMapper) {
        super(modelMapper, LocationEntity.class, Location.class);
    }

    @Override
    protected Location createDto(final LocationEntity source) {
        return new Location(
                source.getId(),
                source.getDateTime(),
                mapNullableCoordinate(source),
                source.getCourse(),
                source.getSpeed(),
                source.getAltitude(),
                source.getSatelliteCount(),
                source.getHdop(),
                source.getInputs(),
                source.getOutputs(),
                source.getAnalogInputs(),
                source.getDriverKeyCode(),
                mapParameters(source),
                mapTracker(source),
                mapAddress(source)
        );
    }

    @Override
    protected void mapSpecificFields(final Location source, final LocationEntity destination) {
        mapCoordinate(source, destination);
        mapParameters(source, destination);
    }

    private static GpsCoordinate mapNullableCoordinate(final LocationEntity source) {
        final LocationEntity.GpsCoordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? map(sourceCoordinate) : null;
    }

    private static GpsCoordinate map(final LocationEntity.GpsCoordinate source) {
        return new GpsCoordinate(source.getLatitude(), source.getLongitude());
    }

    private Map<String, Parameter> mapParameters(final LocationEntity source) {
        return mapLazyToMap(source.getParameters(), Parameter.class, Parameter::getName);
    }

    private Tracker mapTracker(final LocationEntity source) {
        return mapLazy(source.getTracker(), Tracker.class);
    }

    private Address mapAddress(final LocationEntity source) {
        return mapLazy(source.getAddress(), Address.class);
    }

    private void mapCoordinate(final Location source, final LocationEntity destination) {
        destination.setCoordinate(mapNullableCoordinate(source));
    }

    private void mapParameters(final Location source, final LocationEntity destination) {
        destination.setParameters(mapNullableParameters(source));
    }

    private static LocationEntity.GpsCoordinate mapNullableCoordinate(final Location source) {
        final GpsCoordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? map(sourceCoordinate) : null;
    }

    private static LocationEntity.GpsCoordinate map(final GpsCoordinate source) {
        return new LocationEntity.GpsCoordinate(source.getLatitude(), source.getLongitude());
    }

    private List<ParameterEntity> mapNullableParameters(final Location source) {
        final Map<String, Parameter> parametersByNames = source.getParametersByNames();
        return parametersByNames != null ? map(parametersByNames) : null;
    }

    private List<ParameterEntity> map(final Map<String, Parameter> parametersByNames) {
        return CollectionUtil.collectValuesToList(parametersByNames, parameter -> map(parameter, ParameterEntity.class));
    }
}
