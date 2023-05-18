package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.*;
import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.longitudeTypeValue;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AbstractKafkaDataConsumerTest {

    @Test
    public void a() {
        throw new RuntimeException();
    }

//    @Test
//    public void genericRecordShouldBeMappedToData() {
//        final Long givenId = 255L;
//
//        final LocalDateTime givenDateTime = LocalDateTime.of(
//                2023, 4, 8, 18, 25, 2);
//        final long givenEpochSeconds = givenDateTime.toEpochSecond(UTC);
//
//        final Data.Latitude givenLatitude = createLatitude(44, 45, 46, NORTH);
//        final Data.Longitude givenLongitude = createLongitude(47, 48, 49, EAST);
//        final int givenSpeed = 50;
//        final int givenCourse = 51;
//        final int givenAltitude = 52;
//        final int givenAmountOfSatellites = 53;
//        final double givenReductionPrecision = 0.5;
//        final int givenInputs = 54;
//        final int givenOutputs = 55;
//        final String givenDriverKeyCode = "driver-key-code";
//
//        final Long givenTrackerId = 256L;
//        final Tracker givenTracker = createTracker(givenTrackerId);
//
//        final GenericRecord givenGenericRecord = createGenericRecord(givenId, givenEpochSeconds, givenLatitude,
//                givenLongitude, givenSpeed, givenCourse, givenAltitude, givenAmountOfSatellites,
//                givenReductionPrecision, givenInputs, givenOutputs, "5.5,4343.454544334,454.433,1",
//                givenDriverKeyCode, "122:3:str,123:2:6,124:1:7", givenTrackerId
//        );
//
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));
//
//        final Address givenAddress = createAddress(258L);
//        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude))
//                .thenReturn(Optional.of(givenAddress));
//
//        final Data actual = this.consumer.mapToData(givenGenericRecord);
//
//        final double[] expectedAnalogInputs = new double[]{5.5, 4343.454544334, 454.433, 1};
//        final Map<String, Parameter> expectedParametersByNames = Map.of(
//                "122", createParameter("122", STRING, "str"),
//                "123", createParameter("123", DOUBLE, "6"),
//                "124", createParameter("124", INTEGER, "7")
//        );
//        final Data expected = Data.builder()
//                .id(givenId)
//                .date(givenDateTime.toLocalDate())
//                .time(givenDateTime.toLocalTime())
//                .latitude(givenLatitude)
//                .longitude(givenLongitude)
//                .speed(givenSpeed)
//                .course(givenCourse)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .reductionPrecision(givenReductionPrecision)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .analogInputs(expectedAnalogInputs)
//                .driverKeyCode(givenDriverKeyCode)
//                .parametersByNames(expectedParametersByNames)
//                .tracker(givenTracker)
//                .address(givenAddress)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test(expected = DataConsumingException.class)
//    public void genericRecordShouldNotBeMappedToDataBecauseOfThereIsNoTrackerWithGivenId() {
//        final Long givenId = 255L;
//
//        final LocalDateTime givenDateTime = LocalDateTime.of(
//                2023, 4, 8, 18, 25, 2);
//        final long givenEpochSeconds = givenDateTime.toEpochSecond(UTC);
//
//        final Data.Latitude givenLatitude = createLatitude(44, 45, 46, NORTH);
//        final Data.Longitude givenLongitude = createLongitude(47, 48, 49, EAST);
//        final int givenSpeed = 50;
//        final int givenCourse = 51;
//        final int givenAltitude = 52;
//        final int givenAmountOfSatellites = 53;
//        final double givenReductionPrecision = 0.5;
//        final int givenInputs = 54;
//        final int givenOutputs = 55;
//        final String givenDriverKeyCode = "driver-key-code";
//        final Long givenTrackerId = 256L;
//
//        final GenericRecord givenGenericRecord = createGenericRecord(givenId, givenEpochSeconds, givenLatitude,
//                givenLongitude, givenSpeed, givenCourse, givenAltitude, givenAmountOfSatellites,
//                givenReductionPrecision, givenInputs, givenOutputs, "5.5,4343.454544334,454.433,1",
//                givenDriverKeyCode, "122:3:str,123:2:6,124:1:7", givenTrackerId
//        );
//
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(empty());
//
//        this.consumer.mapToData(givenGenericRecord);
//    }
//
//    @Test(expected = DataConsumingException.class)
//    public void genericRecordShouldNotBeMappedToDataBecauseOfGeocodingServiceDoNotFindAddress() {
//        final Long givenId = 255L;
//
//        final LocalDateTime givenDateTime = LocalDateTime.of(
//                2023, 4, 8, 18, 25, 2);
//        final long givenEpochSeconds = givenDateTime.toEpochSecond(UTC);
//
//        final Data.Latitude givenLatitude = createLatitude(44, 45, 46, NORTH);
//        final Data.Longitude givenLongitude = createLongitude(47, 48, 49, EAST);
//        final int givenSpeed = 50;
//        final int givenCourse = 51;
//        final int givenAltitude = 52;
//        final int givenAmountOfSatellites = 53;
//        final double givenReductionPrecision = 0.5;
//        final int givenInputs = 54;
//        final int givenOutputs = 55;
//        final String givenDriverKeyCode = "driver-key-code";
//
//        final Long givenTrackerId = 256L;
//        final Tracker givenTracker = createTracker(givenTrackerId);
//
//        final GenericRecord givenGenericRecord = createGenericRecord(givenId, givenEpochSeconds, givenLatitude,
//                givenLongitude, givenSpeed, givenCourse, givenAltitude, givenAmountOfSatellites,
//                givenReductionPrecision, givenInputs, givenOutputs, "5.5,4343.454544334,454.433,1",
//                givenDriverKeyCode, "122:3:str,123:2:6,124:1:7", givenTrackerId
//        );
//
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));
//
//        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude)).thenReturn(empty());
//
//        this.consumer.mapToData(givenGenericRecord);
//    }
//
//    @Test
//    public void genericRecordWithEmptyAnalogInputsShouldBeMappedToData() {
//        final Long givenId = 255L;
//
//        final LocalDateTime givenDateTime = LocalDateTime.of(
//                2023, 4, 8, 18, 25, 2);
//        final long givenEpochSeconds = givenDateTime.toEpochSecond(UTC);
//
//        final Data.Latitude givenLatitude = createLatitude(44, 45, 46, NORTH);
//        final Data.Longitude givenLongitude = createLongitude(47, 48, 49, EAST);
//        final int givenSpeed = 50;
//        final int givenCourse = 51;
//        final int givenAltitude = 52;
//        final int givenAmountOfSatellites = 53;
//        final double givenReductionPrecision = 0.5;
//        final int givenInputs = 54;
//        final int givenOutputs = 55;
//        final String givenDriverKeyCode = "driver-key-code";
//
//        final Long givenTrackerId = 256L;
//        final Tracker givenTracker = createTracker(givenTrackerId);
//
//        final GenericRecord givenGenericRecord = createGenericRecord(givenId, givenEpochSeconds, givenLatitude,
//                givenLongitude, givenSpeed, givenCourse, givenAltitude, givenAmountOfSatellites,
//                givenReductionPrecision, givenInputs, givenOutputs, "",
//                givenDriverKeyCode, "122:3:str,123:2:6,124:1:7", givenTrackerId
//        );
//
//        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));
//
//        final Address givenAddress = createAddress(258L);
//        when(this.mockedGeocodingService.receive(givenLatitude, givenLongitude))
//                .thenReturn(Optional.of(givenAddress));
//
//        final Data actual = this.consumer.mapToData(givenGenericRecord);
//
//        final double[] expectedAnalogInputs = new double[]{};
//        final Map<String, Parameter> expectedParametersByNames = Map.of(
//                "122", createParameter("122", STRING, "str"),
//                "123", createParameter("123", DOUBLE, "6"),
//                "124", createParameter("124", INTEGER, "7")
//        );
//        final Data expected = Data.builder()
//                .id(givenId)
//                .date(givenDateTime.toLocalDate())
//                .time(givenDateTime.toLocalTime())
//                .latitude(givenLatitude)
//                .longitude(givenLongitude)
//                .speed(givenSpeed)
//                .course(givenCourse)
//                .altitude(givenAltitude)
//                .amountOfSatellites(givenAmountOfSatellites)
//                .reductionPrecision(givenReductionPrecision)
//                .inputs(givenInputs)
//                .outputs(givenOutputs)
//                .analogInputs(expectedAnalogInputs)
//                .driverKeyCode(givenDriverKeyCode)
//                .parametersByNames(expectedParametersByNames)
//                .tracker(givenTracker)
//                .address(givenAddress)
//                .build();
//        assertEquals(expected, actual);
//    }

//    private static GenericRecord createGenericRecord(final Long id, final long epochSeconds,
//                                                     final Data.Latitude latitude, final Data.Longitude longitude,
//                                                     final int speed, final int course, final int altitude,
//                                                     final int amountOfSatellites, final double reductionPrecision,
//                                                     final int inputs, final int outputs,
//                                                     final String serializedAnalogInputs, final String driverKeyCode,
//                                                     final String serializedParameters, final Long trackerId) {
//        final GenericRecord genericRecord = mock(GenericRecord.class);
//        when(genericRecord.get(TransportableData.Fields.id)).thenReturn(id);
//        when(genericRecord.get(TransportableData.Fields.epochSeconds)).thenReturn(epochSeconds);
//        injectLatitudeInGenericRecord(genericRecord, latitude);
//        injectLongitudeInGenericRecord(genericRecord, longitude);
//        when(genericRecord.get(TransportableData.Fields.speed)).thenReturn(speed);
//        when(genericRecord.get(TransportableData.Fields.course)).thenReturn(course);
//        when(genericRecord.get(TransportableData.Fields.altitude)).thenReturn(altitude);
//        when(genericRecord.get(TransportableData.Fields.amountOfSatellites)).thenReturn(amountOfSatellites);
//        when(genericRecord.get(TransportableData.Fields.reductionPrecision)).thenReturn(reductionPrecision);
//        when(genericRecord.get(TransportableData.Fields.inputs)).thenReturn(inputs);
//        when(genericRecord.get(TransportableData.Fields.outputs)).thenReturn(outputs);
//        when(genericRecord.get(TransportableData.Fields.serializedAnalogInputs)).thenReturn(serializedAnalogInputs);
//        when(genericRecord.get(TransportableData.Fields.driverKeyCode)).thenReturn(driverKeyCode);
//        when(genericRecord.get(TransportableData.Fields.serializedParameters)).thenReturn(serializedParameters);
//        when(genericRecord.get(TransportableData.Fields.trackerId)).thenReturn(trackerId);
//        return genericRecord;
//    }
//
//    private static void injectLatitudeInGenericRecord(final GenericRecord genericRecord, final Data.Latitude latitude) {
//        when(genericRecord.get(latitudeDegrees)).thenReturn(latitude.getDegrees());
//        when(genericRecord.get(latitudeMinutes)).thenReturn(latitude.getMinutes());
//        when(genericRecord.get(latitudeMinuteShare)).thenReturn(latitude.getMinuteShare());
//        when(genericRecord.get(latitudeTypeValue)).thenReturn((int) latitude.getType().getValue());
//    }
//
//    private static void injectLongitudeInGenericRecord(final GenericRecord genericRecord, final Data.Longitude longitude) {
//        when(genericRecord.get(longitudeDegrees)).thenReturn(longitude.getDegrees());
//        when(genericRecord.get(longitudeMinutes)).thenReturn(longitude.getMinutes());
//        when(genericRecord.get(longitudeMinuteShare)).thenReturn(longitude.getMinuteShare());
//        when(genericRecord.get(longitudeTypeValue)).thenReturn((int) longitude.getType().getValue());
//    }
}
