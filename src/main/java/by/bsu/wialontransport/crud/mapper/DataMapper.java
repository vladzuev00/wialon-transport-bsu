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
//        return new Data(
//                source.getId(),
//                source.getDateTime(),
//                mapNullableCoordinate(source),
//                source.getCourse(),
//                source.getSpeed(),
//                source.getAltitude(),
//                source.getAmountOfSatellites(),
//                source.getReductionPrecision(),
//                source.getInputs(),
//                source.getOutputs(),
//                source.getAnalogInputs(),
//                source.getDriverKeyCode(),
//                this.mapParameters(source),
//                this.mapTracker(source),
//                this.mapAddress(source)
//        );
        return null;
    }

    @Override
    protected void mapSpecificFields(final Data source, final DataEntity destination) {
//        this.mapCoordinateAndSet(source, destination);
//        this.mapParametersAndSet(source, destination);
    }

    private static Coordinate mapNullableCoordinate(final DataEntity source) {
        final DataEntity.Coordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? mapCoordinate(sourceCoordinate) : null;
    }

    private static Coordinate mapCoordinate(final DataEntity.Coordinate source) {
        final double latitude = source.getLatitude();
        final double longitude = source.getLongitude();
        return new Coordinate(latitude, longitude);
    }

//    private Map<String, Parameter> mapParameters(final DataEntity source) {
//        return super.mapLazyToMap(
//                source,
//                DataEntity::getParameters,
//                Parameter.class,
//                Parameter::getName
//        );
//    }
//
//    private Tracker mapTracker(final DataEntity source) {
//        return super.mapLazy(source, DataEntity::getTracker, Tracker.class);
//    }
//
//    private Address mapAddress(final DataEntity source) {
//        return super.mapLazy(source, DataEntity::getAddress, Address.class);
//    }
//
//    private void mapCoordinateAndSet(final Data source, final DataEntity entity) {
//        super.mapPropertyAndSet(
//                source,
//                DataMapper::mapNullableCoordinate,
//                entity,
//                DataEntity::setCoordinate
//        );
//    }
//
//    private void mapParametersAndSet(final Data source, final DataEntity entity) {
//        super.mapPropertyAndSet(
//                source,
//                this::mapNullableParameters,
//                entity,
//                DataEntity::setParameters
//        );
//    }

    private static DataEntity.Coordinate mapNullableCoordinate(final Data source) {
        final Coordinate sourceCoordinate = source.getCoordinate();
        return sourceCoordinate != null ? mapCoordinate(sourceCoordinate) : null;
    }

    private static DataEntity.Coordinate mapCoordinate(final Coordinate source) {
        final double latitude = source.getLatitude();
        final double longitude = source.getLongitude();
        return new DataEntity.Coordinate(latitude, longitude);
    }

    private List<ParameterEntity> mapNullableParameters(final Data source) {
        final Map<String, Parameter> parametersByNames = source.getParametersByNames();
        return parametersByNames != null ? this.mapParameters(parametersByNames) : null;
    }

    private List<ParameterEntity> mapParameters(final Map<String, Parameter> parametersByNames) {
        return collectValuesToList(parametersByNames, parameter -> super.map(parameter, ParameterEntity.class));
    }
}
