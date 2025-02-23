package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import by.vladzuev.locationreceiver.crud.entity.LocationEntity.GpsCoordinate;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.util.entity.LocationEntityUtil;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Long.MAX_VALUE;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;

public final class LocationRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private LocationRepository repository;

    @Test
    public void locationShouldBeFoundById() {
        final Long givenId = 256L;

        final Optional<LocationEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final LocationEntity actual = optionalActual.get();
        assertFetchedLazily(actual);
        final LocationEntity expected = new LocationEntity(
                givenId,
                LocalDateTime.of(2019, 10, 24, 14, 39, 52),
                new GpsCoordinate(53.233, 27.3434),
                8,
                9,
                10,
                11,
                12.4,
                13,
                14,
                new double[]{0.2, 0.3, 0.4},
                "driver key code",
                List.of(ParameterEntity.builder().id(257L).build()),
                TrackerEntity.builder().id(255L).build(),
                AddressEntity.builder().id(257L).build()
        );
        LocationEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void locationShouldBeSaved() {
        final LocationEntity givenLocation = LocationEntity.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
                .coordinate(new GpsCoordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .satelliteCount(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .tracker(TrackerEntity.builder().id(255L).build())
                .address(AddressEntity.builder().id(258L).build())
                .build();

        final LocationEntity actual = repository.save(givenLocation);
        final LocationEntity expected = LocationEntity.builder()
                .id(1L)
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
                .coordinate(new GpsCoordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .satelliteCount(11)
                .hdop(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .tracker(TrackerEntity.builder().id(255L).build())
                .address(AddressEntity.builder().id(258L).build())
                .build();
        LocationEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void allLocationsShouldBeStreamedByUserIdFetchingTrackerAndAddress() {
        final Long givenUserId = 255L;
        final LocalDateTime givenStartDateTime = LocalDateTime.of(2019, 10, 23, 0, 0, 0);
        final LocalDateTime givenEndDateTime = LocalDateTime.of(2019, 10, 25, 0, 0, 0);
        try (
                final Stream<LocationEntity> actual = repository.streamAllByUserIdFetchingTrackerAndAddress(
                        givenUserId,
                        givenStartDateTime,
                        givenEndDateTime
                )
        ) {
            final Set<LocationEntity> actualAsSet = actual.collect(toSet());
            actualAsSet.forEach(this::assertFetchedOnlyTrackerAndAddress);
            final Set<LocationEntity> expectedAsSet = Set.of(
                    LocationEntity.builder().id(254L).build(),
                    LocationEntity.builder().id(255L).build(),
                    LocationEntity.builder().id(256L).build()
            );
            assertEquals(expectedAsSet, actualAsSet);
        }
    }

    @Test
    public void trackerLastLocationShouldBeFound() {
        final Long givenTrackerId = 255L;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLast(givenTrackerId);
        assertTrue(optionalActual.isPresent());
        final LocationEntity actual = optionalActual.get();
        assertFetchedLazily(actual);
        final LocationEntity expected = LocationEntity.builder().id(257L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerLastLocationShouldNotBeFoundBecauseOfNoTrackerLocations() {
        final Long givenTrackerId = 256L;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLastFetchingParameters(givenTrackerId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerLastLocationShouldNotBeFoundBecauseOfNoSuchTracker() {
        final Long givenTrackerId = MAX_VALUE;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLastFetchingParameters(givenTrackerId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerLastLocationShouldBeFoundFetchingParameters() {
        final Long givenTrackerId = 255L;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLastFetchingParameters(givenTrackerId);
        assertTrue(optionalActual.isPresent());
        final LocationEntity actual = optionalActual.get();
        assertFetchedOnlyParameters(actual);
        final LocationEntity expected = LocationEntity.builder().id(257L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerLastLocationShouldNotBeFoundFetchingParametersBecauseOfNoTrackerLocations() {
        final Long givenTrackerId = 256L;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLastFetchingParameters(givenTrackerId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerLastLocationShouldNotBeFoundFetchingParametersBecauseOfNoSuchTracker() {
        final Long givenTrackerId = MAX_VALUE;

        final Optional<LocationEntity> optionalActual = repository.findTrackerLastFetchingParameters(givenTrackerId);
        assertTrue(optionalActual.isEmpty());
    }

    private void assertFetchedLazily(final LocationEntity entity) {
        assertFalse(Hibernate.isInitialized(entity.getParameters()));
        assertFalse(Hibernate.isInitialized(entity.getTracker()));
        assertFalse(Hibernate.isInitialized(entity.getAddress()));
    }

    private void assertFetchedOnlyTrackerAndAddress(final LocationEntity entity) {
        assertFalse(Hibernate.isInitialized(entity.getParameters()));
        assertTrue(Hibernate.isInitialized(entity.getTracker()));
        assertTrue(Hibernate.isInitialized(entity.getAddress()));
    }

    private void assertFetchedOnlyParameters(final LocationEntity entity) {
        assertTrue(Hibernate.isInitialized(entity.getParameters()));
        assertFalse(Hibernate.isInitialized(entity.getTracker()));
        assertFalse(Hibernate.isInitialized(entity.getAddress()));
    }
}
