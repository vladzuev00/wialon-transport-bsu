package by.bsu.wialontransport.protocol.core.service.receivingdata.filter;

import by.bsu.wialontransport.configuration.property.DataValidationConfiguration;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static java.lang.Double.compare;
import static java.lang.Double.parseDouble;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
public final class DataPropertyValidator {
    private final DataValidationConfiguration validationProperty;

    public boolean isValidAmountOfSatellites(final Data data) {
        return this.validationProperty.getMinValidAmountOfSatellites() <= data.getAmountOfSatellites()
                && data.getAmountOfSatellites() <= this.validationProperty.getMaxValidAmountSatellites();
    }

    public boolean isValidDateTime(final Data data) {
//        final LocalDateTime research = LocalDateTime.of(data.getDate(), data.getTime());
//        final LocalDateTime minAllowableDateTime = this.validationProperty.getMinValidDateTime();
//        final LocalDateTime maxAllowableDateTime = this.findMaxAllowableDateTime();
//        return research.isAfter(minAllowableDateTime) && research.isBefore(maxAllowableDateTime);
        return false;
    }

    public boolean isValidDOPParameters(final Data data) {
        return stream(DOPParameterDictionary.values())
                .map(dictionary -> findDOPParameter(data, dictionary))
                .map(optionalParameter -> optionalParameter.map(this::isValidDOPParameter))
                .allMatch(optionalValidationResult ->
                        optionalValidationResult.isPresent() && optionalValidationResult.get());
    }

    private LocalDateTime findMaxAllowableDateTime() {
        return now().plusSeconds(this.validationProperty.getDeltaSecondsFromNowMaxAllowableValidDateTime());
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
