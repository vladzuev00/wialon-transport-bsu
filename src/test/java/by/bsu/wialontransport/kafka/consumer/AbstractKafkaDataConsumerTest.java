package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.kafka.consumer.AbstractKafkaDataConsumer.*;
import static by.bsu.wialontransport.kafka.transportable.TransportableData.Fields.*;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractKafkaDataConsumerTest {

    @Mock
    private TrackerService mockedTrackerService;

    private TestKafkaDataConsumer consumer;

    @Before
    public void initializeConsumer() {
        this.consumer = new TestKafkaDataConsumer(this.mockedTrackerService);
    }

    @Test
    public void dateTimeShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final long givenEpochSeconds = 100500600;
        when(givenGenericRecord.get(epochSeconds)).thenReturn(givenEpochSeconds);

        final LocalDateTime actual = extractDateTime(givenGenericRecord);
        final LocalDateTime expected = ofEpochSecond(givenEpochSeconds, 0, UTC);
        assertEquals(expected, actual);
    }

    @Test
    public void latitudeShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenLatitudeDegrees = 1;
        when(givenGenericRecord.get(latitudeDegrees)).thenReturn(givenLatitudeDegrees);

        final int givenLatitudeMinutes = 2;
        when(givenGenericRecord.get(latitudeMinutes)).thenReturn(givenLatitudeMinutes);

        final int givenLatitudeMinuteShare = 3;
        when(givenGenericRecord.get(latitudeMinuteShare)).thenReturn(givenLatitudeMinuteShare);

        final int givenLatitudeTypeValueAsInt = 'N';
        when(givenGenericRecord.get(latitudeTypeValue)).thenReturn(givenLatitudeTypeValueAsInt);

        final Latitude actual = this.consumer.extractLatitude(givenGenericRecord);
        final Latitude expected = createLatitude(
                givenLatitudeDegrees, givenLatitudeMinutes, givenLatitudeMinuteShare, NORTH
        );
        assertEquals(expected, actual);
    }

    @Test
    public void longitudeShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenLongitudeDegrees = 1;
        when(givenGenericRecord.get(longitudeDegrees)).thenReturn(givenLongitudeDegrees);

        final int givenLongitudeMinutes = 2;
        when(givenGenericRecord.get(longitudeMinutes)).thenReturn(givenLongitudeMinutes);

        final int givenLongitudeMinuteShare = 3;
        when(givenGenericRecord.get(longitudeMinuteShare)).thenReturn(givenLongitudeMinuteShare);

        final int givenLongitudeTypeValueAsInt = 'E';
        when(givenGenericRecord.get(longitudeTypeValue)).thenReturn(givenLongitudeTypeValueAsInt);

        final Longitude actual = this.consumer.extractLongitude(givenGenericRecord);
        final Longitude expected = createLongitude(
                givenLongitudeDegrees, givenLongitudeMinutes, givenLongitudeMinuteShare, EAST
        );
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenSpeed = 60;
        when(givenGenericRecord.get(speed)).thenReturn(givenSpeed);

        final int actual = extractSpeed(givenGenericRecord);
        assertEquals(givenSpeed, actual);
    }

    @Test
    public void courseShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenCourse = 60;
        when(givenGenericRecord.get(course)).thenReturn(givenCourse);

        final int actual = extractCourse(givenGenericRecord);
        assertEquals(givenCourse, actual);
    }

    @Test
    public void altitudeShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenAltitude = 50;
        when(givenGenericRecord.get(altitude)).thenReturn(givenAltitude);

        final int actual = extractAltitude(givenGenericRecord);
        assertEquals(givenAltitude, actual);
    }

    @Test
    public void amountOfSatellitesShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenAmountOfSatellites = 50;
        when(givenGenericRecord.get(amountOfSatellites)).thenReturn(givenAmountOfSatellites);

        final int actual = extractAmountOfSatellites(givenGenericRecord);
        assertEquals(givenAmountOfSatellites, actual);
    }

    @Test
    public void reductionPrecisionShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final double givenReductionPrecision = 0.5;
        when(givenGenericRecord.get(reductionPrecision)).thenReturn(givenReductionPrecision);

        final double actual = extractReductionPrecision(givenGenericRecord);
        assertEquals(givenReductionPrecision, actual, 0.);
    }

    @Test
    public void inputsShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenInputs = 10;
        when(givenGenericRecord.get(inputs)).thenReturn(givenInputs);

        final int actual = extractInputs(givenGenericRecord);
        assertEquals(givenInputs, actual);
    }

    @Test
    public void outputsShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final int givenOutputs = 15;
        when(givenGenericRecord.get(outputs)).thenReturn(givenOutputs);

        final int actual = extractOutputs(givenGenericRecord);
        assertEquals(givenOutputs, actual);
    }

    @Test
    public void analogInputsShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final String givenSerializedAnalogInputs = "5.5,4343.454544334,454.433,1";
        when(givenGenericRecord.get(serializedAnalogInputs)).thenReturn(givenSerializedAnalogInputs);

        final double[] actual = this.consumer.extractAnalogInputs(givenGenericRecord);
        final double[] expected = new double[]{5.5, 4343.454544334, 454.433, 1};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void emptyAnalogInputsShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final String givenSerializedAnalogInputs = "";
        when(givenGenericRecord.get(serializedAnalogInputs)).thenReturn(givenSerializedAnalogInputs);

        final double[] actual = this.consumer.extractAnalogInputs(givenGenericRecord);
        final double[] expected = new double[]{};
        assertArrayEquals(expected, actual, 0.);
    }

    @Test
    public void driverKeyCodeShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final String givenDriverKeyCode = "driverKeyCode";
        when(givenGenericRecord.get(driverKeyCode)).thenReturn(givenDriverKeyCode);

        final String actual = extractDriverKeyCode(givenGenericRecord);
        assertEquals(givenDriverKeyCode, actual);
    }

    @Test
    public void parametersByNamesShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final String givenSerializedParameters = "122:3:str,256:123:2:6,124:1:7";
        when(givenGenericRecord.get(serializedParameters)).thenReturn(givenSerializedParameters);

        final Map<String, Parameter> actual = this.consumer.extractParametersByNames(givenGenericRecord);
        final Map<String, Parameter> expected = Map.of(
                "122", createParameter("122", STRING, "str"),
                "123", createParameter(256L, "123", DOUBLE, "6"),
                "124", createParameter("124", INTEGER, "7")
        );
        assertEquals(expected, actual);
    }

    @Test
    public void emptyParametersByNamesShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final String givenSerializedParameters = "";
        when(givenGenericRecord.get(serializedParameters)).thenReturn(givenSerializedParameters);

        final Map<String, Parameter> actual = this.consumer.extractParametersByNames(givenGenericRecord);
        final Map<String, Parameter> expected = emptyMap();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeExtracted() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final Long givenTrackerId = 255L;
        when(givenGenericRecord.get(trackerId)).thenReturn(givenTrackerId);

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));

        final Tracker actual = this.consumer.extractTracker(givenGenericRecord);
        assertEquals(givenTracker, actual);
    }

    @Test(expected = DataConsumingException.class)
    public void trackerShouldNotBeExtractedBecauseOfThereIsNoTrackerWithGivenId() {
        final GenericRecord givenGenericRecord = mock(GenericRecord.class);

        final Long givenTrackerId = 255L;
        when(givenGenericRecord.get(trackerId)).thenReturn(givenTrackerId);

        createTracker(givenTrackerId);
        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(empty());

        this.consumer.extractTracker(givenGenericRecord);
    }

    private static Latitude createLatitude(final int degrees,
                                           final int minutes,
                                           final int minuteShare,
                                           final DataEntity.Latitude.Type type) {
        return Latitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Longitude createLongitude(final int degrees,
                                             final int minutes,
                                             final int minuteShare,
                                             final DataEntity.Longitude.Type type) {
        return Longitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static Parameter createParameter(final String name,
                                             final ParameterEntity.Type type,
                                             final String value) {
        return Parameter.builder()
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    private static Parameter createParameter(final Long id,
                                             final String name,
                                             final ParameterEntity.Type type,
                                             final String value) {
        return Parameter.builder()
                .id(id)
                .name(name)
                .type(type)
                .value(value)
                .build();
    }

    private static final class TestKafkaDataConsumer extends AbstractKafkaDataConsumer {

        public TestKafkaDataConsumer(final TrackerService trackerService) {
            super(trackerService);
        }

        @Override
        protected Data mapToData(final GenericRecord value) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void processData(final List<Data> data) {
            throw new UnsupportedOperationException();
        }
    }
}
