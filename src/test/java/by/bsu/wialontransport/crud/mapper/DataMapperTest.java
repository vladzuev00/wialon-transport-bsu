package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.model.Coordinate;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.util.entity.DataEntityUtil.checkEquals;
import static java.util.Collections.emptyList;
import static org.hibernate.Hibernate.isInitialized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class DataMapperTest extends AbstractContextTest {

    @Autowired
    private DataMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Data givenDto = Data.builder()
                .id(255L)
                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                .coordinate(new Coordinate(5.5, 6.6))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(
                        Map.of(
                                "first-param", createParameterDto(256L, "first-param"),
                                "second-param", createParameterDto(257L, "second-param")
                        )
                )
                .tracker(createTrackerDto(258L))
                .address(createAddressDto(259L))
                .build();

        final DataEntity actual = mapper.mapToEntity(givenDto);
        final DataEntity expected = DataEntity.builder()
                .id(255L)
                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                .coordinate(new DataEntity.Coordinate(5.5, 6.6))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(
                        List.of(
                                createParameterEntity(256L, "first-param"),
                                createParameterEntity(257L, "second-param")
                        )
                )
                .tracker(createTrackerEntity(258L))
                .address(createAddressEntity(259L))
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityWithLoadedPropertiesShouldBeMappedToDto() {
        final DataEntity givenEntity = DataEntity.builder()
                .id(255L)
                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                .coordinate(new DataEntity.Coordinate(5.5, 6.6))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(
                        List.of(
                                createParameterEntity(256L, "first-param"),
                                createParameterEntity(257L, "second-param")
                        )
                )
                .tracker(createTrackerEntity(258L))
                .address(createAddressEntity(259L))
                .build();

        final Data actual = mapper.mapToDto(givenEntity);
        final Data expected = Data.builder()
                .id(255L)
                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                .coordinate(new Coordinate(5.5, 6.6))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "first-param", createParameterDto(256L, "first-param"),
                        "second-param", createParameterDto(257L, "second-param")
                ))
                .tracker(createTrackerDto(258L))
                .address(createAddressDto(259L))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
        final List<ParameterEntity> givenParameters = emptyList();
        final TrackerEntity givenTracker = new TrackerEntity();
        final AddressEntity givenAddress = new AddressEntity();
        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
            mockedStatic
                    .when(() -> isInitialized(same(givenParameters)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenTracker)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenAddress)))
                    .thenReturn(false);

            final DataEntity givenEntity = DataEntity.builder()
                    .id(255L)
                    .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                    .coordinate(new DataEntity.Coordinate(5.5, 6.6))
                    .speed(26)
                    .course(27)
                    .altitude(28)
                    .amountOfSatellites(29)
                    .reductionPrecision(30.5)
                    .inputs(31)
                    .outputs(32)
                    .analogInputs(new double[]{0.2, 0.3, 0.4})
                    .driverKeyCode("driver key code")
                    .parameters(givenParameters)
                    .tracker(givenTracker)
                    .address(givenAddress)
                    .build();

            final Data actual = mapper.mapToDto(givenEntity);
            final Data expected = Data.builder()
                    .id(255L)
                    .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
                    .coordinate(new Coordinate(5.5, 6.6))
                    .speed(26)
                    .course(27)
                    .altitude(28)
                    .amountOfSatellites(29)
                    .reductionPrecision(30.5)
                    .inputs(31)
                    .outputs(32)
                    .analogInputs(new double[]{0.2, 0.3, 0.4})
                    .driverKeyCode("driver key code")
                    .build();
            assertEquals(expected, actual);
        }
    }

    private static Parameter createParameterDto(final Long id, final String name) {
        return Parameter.builder()
                .id(id)
                .name(name)
                .build();
    }

    private static ParameterEntity createParameterEntity(final Long id, final String name) {
        return ParameterEntity.builder()
                .id(id)
                .name(name)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTrackerDto(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerEntity createTrackerEntity(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Address createAddressDto(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static AddressEntity createAddressEntity(final Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }
}
