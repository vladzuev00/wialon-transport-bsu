package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.model.Coordinate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.util.CollectionUtil.collectValuesToList;

@Component
public final class DataMapper extends Mapper<DataEntity, Data> {

    public DataMapper(final ModelMapper modelMapper) {
        super(modelMapper, DataEntity.class, Data.class);
    }

    @Override
    protected Data createDto(final DataEntity source) {
        return new Data(
                source.getId(),
                source.getDateTime(),
                mapNullableCoordinate(source),
                source.getCourse(),
                source.getSpeed(),
                source.getAltitude(),
                source.getAmountOfSatellites(),
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
    protected void mapSpecificFields(final Data source, final DataEntity destination) {
        mapCoordinate(source, destination);
        mapParameters(source, destination);
    }

    private static Coordinate mapNullableCoordinate(final DataEntity source) {
        final DataEntity.Coordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? map(sourceCoordinate) : null;
    }

    private static Coordinate map(final DataEntity.Coordinate source) {
        return new Coordinate(source.getLatitude(), source.getLongitude());
    }

    private Map<String, Parameter> mapParameters(final DataEntity source) {
        return mapLazyToMap(source.getParameters(), Parameter.class, Parameter::getName);
    }

    private Tracker mapTracker(final DataEntity source) {
        return mapLazy(source.getTracker(), Tracker.class);
    }

    private Address mapAddress(final DataEntity source) {
        return mapLazy(source.getAddress(), Address.class);
    }

    private void mapCoordinate(final Data source, final DataEntity destination) {
        destination.setCoordinate(mapNullableCoordinate(source));
    }

    private void mapParameters(final Data source, final DataEntity destination) {
        destination.setParameters(mapNullableParameters(source));
    }

    private static DataEntity.Coordinate mapNullableCoordinate(final Data source) {
        final Coordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? map(sourceCoordinate) : null;
    }

    private static DataEntity.Coordinate map(final Coordinate source) {
        return new DataEntity.Coordinate(source.getLatitude(), source.getLongitude());
    }

    private List<ParameterEntity> mapNullableParameters(final Data source) {
        final Map<String, Parameter> parametersByNames = source.getParametersByNames();
        return parametersByNames != null ? map(parametersByNames) : null;
    }

    private List<ParameterEntity> map(final Map<String, Parameter> parametersByNames) {
        return collectValuesToList(parametersByNames, parameter -> map(parameter, ParameterEntity.class));
    }
}
