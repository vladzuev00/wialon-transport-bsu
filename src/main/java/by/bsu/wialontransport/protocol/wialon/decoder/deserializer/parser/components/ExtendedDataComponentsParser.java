package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Byte.parseByte;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;

public final class ExtendedDataComponentsParser extends DataComponentsParser {
    public static final String REGEX_EXTENDED_DATA = REGEX_DATA
            + "((\\d+\\.\\d+)|(NA));"                              //hdop
            + "(\\d+|(NA));"                                       //inputs
            + "(\\d+|(NA));"                                       //outputs
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"                      //analogInputs
            + "(.*);"                                              //driverKeyCode
            + "(([^:]+:[123]:[^:]+,?)*)";                          //parameters;
    public static final Pattern PATTERN_EXTENDED_DATA = compile(REGEX_EXTENDED_DATA);

    public static final int GROUP_NUMBER_REDUCTION_PRECISION = 28;
    public static final int GROUP_NUMBER_INPUTS = 31;
    public static final int GROUP_NUMBER_OUTPUTS = 33;
    public static final int GROUP_NUMBER_ANALOG_INPUTS = 35;
    public static final int GROUP_NUMBER_DRIVER_KEY_CODE = 40;
    public static final int GROUP_NUMBER_PARAMETERS = 41;

    private static final String NOT_DEFINED_REDUCTION_PRECISION_STRING = "NA";
    private static final Double NOT_DEFINED_REDUCTION_PRECISION = Double.MIN_VALUE;

    private static final String NOT_DEFINED_INPUTS_STRING = "NA";
    private static final int NOT_DEFINED_INPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_OUTPUTS_STRING = "NA";
    private static final int NOT_DEFINED_OUTPUTS = Integer.MIN_VALUE;

    private static final String NOT_DEFINED_ANALOG_INPUTS_STRING = "NA";
    private static final String DELIMITER_ANALOG_INPUTS = ",";

    private static final String NOT_DEFINED_DRIVER_KEY_CODE_INBOUND_STRING = "NA";
    private static final String NOT_DEFINED_DRIVER_KEY_CODE = "not defined";

    private static final String DELIMITER_PARAMETERS = ",";
    private static final String DELIMITER_PARAMETER_COMPONENTS = ":";
    private static final int PARAMETER_NAME_INDEX = 0;
    private static final int PARAMETER_TYPE_INDEX = 1;
    private static final int PARAMETER_VALUE_INDEX = 2;

    private final ParameterParser parameterParser;

    public ExtendedDataComponentsParser() {
        this.parameterParser = new ParameterParser();
    }

    public double parseReductionPrecision() {
        final String reductionPrecisionString = super.getMatcher().group(GROUP_NUMBER_REDUCTION_PRECISION);
        return !reductionPrecisionString.equals(NOT_DEFINED_REDUCTION_PRECISION_STRING)
                ? parseDouble(reductionPrecisionString)
                : NOT_DEFINED_REDUCTION_PRECISION;
    }

    public int parseInputs() {
        final String inputsString = super.getMatcher().group(GROUP_NUMBER_INPUTS);
        return !inputsString.equals(NOT_DEFINED_INPUTS_STRING) ? parseInt(inputsString) : NOT_DEFINED_INPUTS;
    }

    public int parseOutputs() {
        final String outputsString = super.getMatcher().group(GROUP_NUMBER_OUTPUTS);
        return !outputsString.equals(NOT_DEFINED_OUTPUTS_STRING) ? parseInt(outputsString) : NOT_DEFINED_OUTPUTS;
    }

    public double[] parseAnalogInputs() {
        final String analogInputsString = super.getMatcher().group(GROUP_NUMBER_ANALOG_INPUTS);
        if (analogInputsString.isEmpty() || analogInputsString.equals(NOT_DEFINED_ANALOG_INPUTS_STRING)) {
            return new double[0];
        }
        return stream(analogInputsString.split(DELIMITER_ANALOG_INPUTS))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    public String parseDriverKeyCode() {
        final String inboundDriverKeyCode = super.getMatcher().group(GROUP_NUMBER_DRIVER_KEY_CODE);
        return !inboundDriverKeyCode.equals(NOT_DEFINED_DRIVER_KEY_CODE_INBOUND_STRING)
                ? inboundDriverKeyCode
                : NOT_DEFINED_DRIVER_KEY_CODE;
    }

    public List<Parameter> parseParameters() {
        final String parametersString = super.getMatcher().group(GROUP_NUMBER_PARAMETERS);
        return !parametersString.isEmpty() ?
                stream(parametersString.split(DELIMITER_PARAMETERS))
                        .map(this.parameterParser::parse)
                        .collect(Collectors.toList())
                : emptyList();
    }

    @Override
    protected Pattern findPattern() {
        return PATTERN_EXTENDED_DATA;
    }

    private static final class ParameterParser {

        public Parameter parse(final String source) {
            final String[] components = source.split(DELIMITER_PARAMETER_COMPONENTS);
            final String name = components[PARAMETER_NAME_INDEX];
            final ParameterEntity.Type type = parseType(components);
            final String value = components[PARAMETER_VALUE_INDEX];
            return Parameter.builder()
                    .name(name)
                    .type(type)
                    .value(value)
                    .build();
        }

        private static ParameterEntity.Type parseType(final String[] components) {
            final String typeString = components[PARAMETER_TYPE_INDEX];
            return ParameterEntity.Type.findByValue(parseByte(typeString));
        }
    }
}
