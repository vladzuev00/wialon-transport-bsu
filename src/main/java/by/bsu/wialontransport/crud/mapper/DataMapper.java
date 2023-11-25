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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.util.CollectionUtil.collectMappedValuesToList;
import static by.bsu.wialontransport.util.CollectionUtil.convertToMap;

@Component
public final class DataMapper extends AbstractMapper<DataEntity, Data> {

    public DataMapper(final ModelMapper modelMapper) {
        super(modelMapper, DataEntity.class, Data.class);
    }

    @Override
    protected Data createDto(final DataEntity entity) {
        return new Data(
                entity.getId(),
                entity.getDateTime(),
                mapCoordinate(entity),
                entity.getCourse(),
                entity.getSpeed(),
                entity.getAltitude(),
                entity.getAmountOfSatellites(),
                entity.getReductionPrecision(),
                entity.getInputs(),
                entity.getOutputs(),
                entity.getAnalogInputs(),
                entity.getDriverKeyCode(),
                this.mapParameters(entity),
                this.mapTracker(entity),
                this.mapAddress(entity)
        );
    }

    @Override
    protected void mapSpecificFields(final Data source, final DataEntity destination) {
        this.mapCoordinateAndSet(source, destination);
        this.mapParametersAndSet(source, destination);
    }

    private static Coordinate mapCoordinate(final DataEntity source) {
        final DataEntity.Coordinate sourceCoordinate = source.getCoordinate();
        final double latitude = sourceCoordinate.getLatitude();
        final double longitude = sourceCoordinate.getLongitude();
        return new Coordinate(latitude, longitude);
    }

    private Map<String, Parameter> mapParameters(final DataEntity source) {
        final List<ParameterEntity> sourceEntities = source.getParameters();
        final List<Parameter> dtos = super.mapCollectionPropertyIfLoadedOrElseNull(
                sourceEntities,
                Parameter.class,
                ArrayList::new
        );
        return dtos != null ? convertToMap(dtos, Parameter::getName) : null;
    }

    private static DataEntity.Coordinate mapCoordinate(final Data source) {
        final Coordinate sourceCoordinate = source.getCoordinate();
        final double latitude = sourceCoordinate.getLatitude();
        final double longitude = sourceCoordinate.getLongitude();
        return new DataEntity.Coordinate(latitude, longitude);
    }

    private List<ParameterEntity> mapParameters(final Data source) {
        final Map<String, Parameter> parametersByNames = source.getParametersByNames();
        return collectMappedValuesToList(
                parametersByNames,
                parameter -> super.mapProperty(parameter, ParameterEntity.class)
        );
    }

    private Tracker mapTracker(final DataEntity source) {
        return super.mapPropertyIfLoadedOrElseNull(source, DataEntity::getTracker, Tracker.class);
    }

    private Address mapAddress(final DataEntity source) {
        return super.mapPropertyIfLoadedOrElseNull(source, DataEntity::getAddress, Address.class);
    }

    private void mapCoordinateAndSet(final Data source, final DataEntity entity) {
        super.mapPropertyAndSet(
                source,
                DataMapper::mapCoordinate,
                entity,
                DataEntity::setCoordinate
        );
    }

    private void mapParametersAndSet(final Data source, final DataEntity entity) {
        super.mapPropertyAndSet(
                source,
                this::mapParameters,
                entity,
                DataEntity::setParameters
        );
    }
}
