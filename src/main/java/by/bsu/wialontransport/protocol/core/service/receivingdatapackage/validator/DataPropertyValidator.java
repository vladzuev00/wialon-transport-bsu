package by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator;

import by.bsu.wialontransport.configuration.property.DataValidationProperty;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary.*;
import static java.lang.Double.compare;
import static java.lang.Double.parseDouble;
import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public final class DataPropertyValidator {
    private final DataValidationProperty validationProperty;

    public boolean isValidAmountOfSatellites(final Data data) {
        return this.validationProperty.getMinValidAmountOfSatellites() <= data.getAmountOfSatellites()
                && data.getAmountOfSatellites() <= this.validationProperty.getMaxValidAmountSatellite();
    }

    public boolean isValidDateTime(final Data data) {
        final LocalDateTime research = LocalDateTime.of(data.getDate(), data.getTime());
        final LocalDateTime maxAllowableDateTime = now()
                .plusSeconds(this.validationProperty.getDeltaSecondsFromNowMaxAllowableValidDateTime());
        return research.isAfter(this.validationProperty.getMinValidDateTime())
                && research.isBefore(maxAllowableDateTime);
    }

    public boolean isValidDOPParameters(final Data data) {
        final Optional<Parameter> optionalHDOP = findDOPParameter(data, HDOP);
        final Optional<Parameter> optionalVDOP = findDOPParameter(data, VDOP);
        final Optional<Parameter> optionalPDOP = findDOPParameter(data, PDOP);
        return Stream.of(optionalHDOP, optionalVDOP, optionalPDOP)
                .map(optionalParameter -> optionalParameter.map(this::isValidDOPParameter))
                .allMatch(optionalValidationResult -> optionalValidationResult.isPresent() && optionalValidationResult.get());
    }

    private static Optional<Parameter> findDOPParameter(final Data data,
                                                        final DOPParameterDictionary parameterDictionary) {
        final Map<String, Parameter> parametersByNames = data.getParametersByNames();
        return parametersByNames.entrySet().stream()
                .filter(parameterByName -> parameterDictionary.getAliases().contains(parameterByName.getKey()))
                .findAny()
                .map(Map.Entry::getValue);
    }

    private boolean isValidDOPParameter(final Parameter parameter) {
        if (parameter.getType() != DOUBLE) {
            return false;
        }
        final double value = parseDouble(parameter.getValue());
        return compare(value, this.validationProperty.getMinValidDOP()) >= 0
                && compare(value, this.validationProperty.getMaxValidDOP()) <= 0;
    }
}
