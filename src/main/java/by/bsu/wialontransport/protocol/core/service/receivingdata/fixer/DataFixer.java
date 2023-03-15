package by.bsu.wialontransport.protocol.core.service.receivingdata.fixer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.exception.DataFixingException;
import by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary.findByAlias;
import static java.util.Arrays.stream;

@Component
public final class DataFixer {
    private static final String MESSAGE_EXCEPTION_PREVIOUS_DATA_DOES_NOT_HAVE_DOP_PARAMETER = "Previous data doesn't "
            + "have DOP parameter - impossible to fix.";

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
        final Map<String, Parameter> fixedParametersByNames
                = findFixedParametersByNamesWithEntriesOfMissingDOPParameters(fixed);
        injectDOPParametersFromPrevious(fixedParametersByNames, previous);
        return fixedParametersByNames;
    }

    private static Map<String, Parameter> findFixedParametersByNamesWithEntriesOfMissingDOPParameters(final Data fixed) {
        final Map<String, Parameter> fixedParametersByNames = new HashMap<>(fixed.getParametersByNames());
        stream(DOPParameterDictionary.values())
                .filter(dictionary -> !isExistEntryOfDOPParameter(dictionary, fixedParametersByNames))
                .forEach(dictionary -> putEntryOfDOPParameter(dictionary, fixedParametersByNames));
        return fixedParametersByNames;
    }

    private static boolean isExistEntryOfDOPParameter(final DOPParameterDictionary dictionary,
                                                      final Map<String, Parameter> parametersByNames) {
        return parametersByNames.keySet()
                .stream()
                .anyMatch(dictionary::isAlias);
    }

    private static void putEntryOfDOPParameter(final DOPParameterDictionary dictionary,
                                               final Map<String, Parameter> parametersByNames) {
        final String name = dictionary.findAnyAlias();
        parametersByNames.put(name, null);
    }

    private static void injectDOPParametersFromPrevious(final Map<String, Parameter> fixedParametersByNames,
                                                        final Data previous) {
        for (final Entry<String, Parameter> parameterByName : fixedParametersByNames.entrySet()) {
            final Optional<DOPParameterDictionary> optionalDictionary = findByAlias(parameterByName.getKey());
            optionalDictionary.ifPresent(
                    dictionary -> injectPreviousDOPParameterInEntry(dictionary, previous, parameterByName));
        }
    }

    private static void injectPreviousDOPParameterInEntry(final DOPParameterDictionary dictionary, final Data previous,
                                                          final Entry<String, Parameter> entry) {
        final Parameter injectedDOPParameter = findDOPParameter(dictionary, previous)
                .orElseThrow(
                        () -> new DataFixingException(MESSAGE_EXCEPTION_PREVIOUS_DATA_DOES_NOT_HAVE_DOP_PARAMETER)
                );
        entry.setValue(injectedDOPParameter);
    }

    private static Optional<Parameter> findDOPParameter(final DOPParameterDictionary dictionary, final Data data) {
        return data.getParametersByNames()
                .entrySet()
                .stream()
                .filter(parameterByName -> dictionary.isAlias(parameterByName.getKey()))
                .map(Entry::getValue)
                .findFirst();
    }
}