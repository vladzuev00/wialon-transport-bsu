package by.bsu.wialontransport.protocol.core.service.receivingdata.fixer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.exception.DataFixingException;
import by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import static by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary.findByAlias;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

//TODO: все не то
@Component
public final class DataFixer {
    private static final String MESSAGE_EXCEPTION_PREVIOUS_DATA_DOES_NOT_HAVE_DOP_PARAMETER = "Previous data doesn't "
            + "have DOP parameter - impossible to fix.";
    private static final String MESSAGE_EXCEPTION_NO_DICTIONARY_FOR_ALIAS = "There is no dictionary for alias '%s'";

    public Data fix(final Data fixed, final Data previous) {
        return Data.builder()
                .id(fixed.getId())
                .date(fixed.getDate())
                .time(fixed.getTime())
                .latitude(previous.getLatitude())
                .longitude(previous.getLongitude())
                .speed(fixed.getSpeed())
                .course(fixed.getCourse())
                .altitude(fixed.getAltitude())
                .amountOfSatellites(previous.getAmountOfSatellites())
                .reductionPrecision(fixed.getReductionPrecision())
                .inputs(fixed.getInputs())
                .outputs(fixed.getOutputs())
                .analogInputs(fixed.getAnalogInputs())
                .driverKeyCode(fixed.getDriverKeyCode())
                .parametersByNames(fixDOPParameters(fixed, previous))
                .tracker(fixed.getTracker())
                .build();
    }

    private static Map<String, Parameter> fixDOPParameters(final Data fixed, final Data previous) {
        final Map<String, Parameter> fixedParametersByNames = new HashMap<>(fixed.getParametersByNames());
        stream(DOPParameterDictionary.values())
                .forEach(dictionary ->
                        fixDOPParameter(dictionary, fixedParametersByNames, previous.getParametersByNames())
                );
        return fixedParametersByNames;
    }

    private static void fixDOPParameter(final DOPParameterDictionary dictionary,
                                        final Map<String, Parameter> fixedParametersByNames,
                                        final Map<String, Parameter> previousParametersByNames) {
        final Parameter previousDOPParameter = previousParametersByNames.entrySet()
                .stream()
                .filter(parameterByName -> dictionary.isAlias(parameterByName.getKey()))
                .map(Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new DataFixingException(MESSAGE_EXCEPTION_PREVIOUS_DATA_DOES_NOT_HAVE_DOP_PARAMETER));
        dictionary.getAliases()
                .forEach(alias -> fixedParametersByNames.compute(
                        alias, createFunctionReplacingParameter(previousDOPParameter)
                ));
    }

    private static BiFunction<String, Parameter, Parameter> createFunctionReplacingParameter(
            final Parameter newParameter) {
        return (alias, oldParameter) -> newParameter;
    }

    private static Map<DOPParameterDictionary, Parameter> findParametersByDictionaries(
            final Map<String, Parameter> parametersByNames) {
        return parametersByNames.entrySet()
                .stream()
                .collect(
                        toMap(
                                parameterByName -> findDictionaryByAlias(parameterByName.getKey()),
                                Entry::getValue
                        )
                );
    }

    private static DOPParameterDictionary findDictionaryByAlias(final String alias) {
        final Optional<DOPParameterDictionary> optionalDOPParameterDictionary = findByAlias(alias);
        return optionalDOPParameterDictionary.orElseThrow(
                () -> new DataFixingException(format(MESSAGE_EXCEPTION_NO_DICTIONARY_FOR_ALIAS, alias)));
    }
}